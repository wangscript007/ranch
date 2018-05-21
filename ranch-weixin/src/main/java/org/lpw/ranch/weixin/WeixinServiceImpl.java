package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.weixin.info.InfoService;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Coder;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.QrCode;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.util.Xml;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(WeixinModel.NAME + ".service")
public class WeixinServiceImpl implements WeixinService, ContextRefreshedListener, HourJob, MinuteJob {
    private static final String SESSION_MINI_SESSION_KEY = WeixinModel.NAME + ".service.mini.session-key";
    @Inject
    private Digest digest;
    @Inject
    private Converter converter;
    @Inject
    private Numeric numeric;
    @Inject
    private Generator generator;
    @Inject
    private Validator validator;
    @Inject
    private Http http;
    @Inject
    private Json json;
    @Inject
    private DateTime dateTime;
    @Inject
    private Xml xml;
    @Inject
    private Sign sign;
    @Inject
    private QrCode qrCode;
    @Inject
    private Coder coder;
    @Inject
    private Logger logger;
    @Inject
    private Storages storages;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Header header;
    @Inject
    private Session session;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private InfoService infoService;
    @Inject
    private WeixinDao weixinDao;
    @Value("${tephra.ctrl.service-root:}")
    private String root;
    @Value("${" + WeixinModel.NAME + ".auto:false}")
    private boolean auto;
    @Value("${" + WeixinModel.NAME + ".synch.url:}")
    private String synchUrl;
    @Value("${" + WeixinModel.NAME + ".synch.key:}")
    private String synchKey;
    @Value("${" + WeixinModel.NAME + ".qr-code.size:256}")
    private int qrCodeSize;
    @Value("${" + WeixinModel.NAME + ".qr-code.logo:}")
    private String qrCodeLogo;
    private String logo;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(weixinDao.query().getList());
    }

    @Override
    public WeixinModel findByKey(String key) {
        return weixinDao.findByKey(key);
    }

    @Override
    public WeixinModel findByAppId(String appId) {
        return weixinDao.findByAppId(appId);
    }

    @Override
    public JSONObject save(WeixinModel weixin) {
        WeixinModel model = weixinDao.findByKey(weixin.getKey());
        if (model == null) {
            model = new WeixinModel();
            model.setKey(weixin.getKey());
        }
        boolean modify = !weixin.getAppId().equals(model.getAppId()) || !weixin.getSecret().equals(model.getSecret());
        model.setName(weixin.getName());
        model.setAppId(weixin.getAppId());
        model.setSecret(weixin.getSecret());
        model.setToken(weixin.getToken());
        model.setMchId(weixin.getMchId());
        model.setMchKey(weixin.getMchKey());
        weixinDao.save(model);
        if (modify && auto && validator.isEmpty(synchUrl))
            update(model);

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String id) {
        weixinDao.delete(id);
    }

    @Override
    public String echo(String appId, String signature, String timestamp, String nonce, String echostr) {
        WeixinModel weixin = weixinDao.findByAppId(appId);
        if (weixin == null)
            return "failure";

        List<String> list = new ArrayList<>();
        list.add(weixin.getToken());
        list.add(timestamp);
        list.add(nonce);
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        list.forEach(sb::append);
        boolean success = digest.sha1(sb.toString()).equals(signature);

        if (logger.isInfoEnable())
            logger.info("验证微信服务[{}:signature={};timestamp={};nonce={};echostr={}]。",
                    success, signature, timestamp, nonce, echostr);

        return success ? echostr : "failure";
    }

    @Override
    public JSONObject auth(String key, String code) {
        WeixinModel weixin = weixinDao.findByKey(key);
        Map<String, String> map = getAuthMap(weixin);
        map.put("code", code);
        JSONObject object = json.toObject(http.get("https://api.weixin.qq.com/sns/oauth2/access_token", null, map));
        if (object == null || !object.containsKey("openid")) {
            logger.warn(null, "获取微信公众号用户认证信息[{}:{}:{}]失败！", key, code, object);

            return new JSONObject();
        }

        String openId = object.getString("openid");
        if (object.containsKey("access_token")) {
            map.clear();
            map.put("access_token", object.getString("access_token"));
            map.put("openid", openId);
            map.put("lang", "zh_CN");
            JSONObject obj = json.toObject(http.get("https://api.weixin.qq.com/sns/userinfo", null, map));
            if (obj != null)
                object.putAll(obj);
        }
        infoService.save(key, weixin.getAppId(), object.getString("unionid"), openId);

        if (logger.isDebugEnable())
            logger.debug("获得微信公众号用户认证信息[{}:{}:{}]。", key, code, object);

        return object;
    }

    @Override
    public JSONObject auth(String key, String code, String iv, String message) {
        WeixinModel weixin = weixinDao.findByKey(key);
        Map<String, String> map = getAuthMap(weixin);
        map.put("js_code", code);
        JSONObject object = json.toObject(http.get("https://api.weixin.qq.com/sns/jscode2session", null, map));
        if (object == null || !object.containsKey("openid")) {
            logger.warn(null, "获取微信小程序用户认证信息[{}:{}:{}]失败！", key, code, object);

            return new JSONObject();
        }

        if (logger.isDebugEnable())
            logger.debug("获得微信小程序用户认证信息[{}:{}:{}]。", key, code, object);

        String sessionKey = object.getString("session_key");
        session.set(SESSION_MINI_SESSION_KEY, sessionKey);
        object.remove("session_key");
        if (validator.isEmpty(iv) || validator.isEmpty(message)) {
            infoService.save(key, weixin.getAppId(), object.getString("unionid"), object.getString("openid"));

            return object;
        }

        object.putAll(decryptAesCbcPkcs7(iv, message));
        object.put("unionid", object.getString("unionId"));
        infoService.save(key, weixin.getAppId(), object.getString("unionid"), object.getString("openid"));

        return object;
    }

    private Map<String, String> getAuthMap(WeixinModel weixin) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", weixin.getAppId());
        map.put("secret", weixin.getSecret());
        map.put("grant_type", "authorization_code");

        return map;
    }

    @Override
    public void prepayQrCode(String key, String user, String subject, int amount, String billNo, String notice, int size,
                             String logo, OutputStream outputStream) {
        Map<String, String> map = prepay(key, user, null, subject, amount, billNo, notice, "NATIVE", new HashMap<>());
        if (map == null)
            return;

        qrCode.create(map.get("code_url"), size > 0 ? size : qrCodeSize, getLogo(logo), outputStream);
    }

    @Override
    public String prepayQrCodeBase64(String key, String user, String subject, int amount, String billNo, String notice,
                                     int size, String logo) {
        Map<String, String> map = prepay(key, user, null, subject, amount, billNo, notice, "NATIVE", new HashMap<>());

        return map == null ? null : qrCode.create(map.get("code_url"), size > 0 ? size : qrCodeSize, getLogo(logo));
    }

    private String getLogo(String logo) {
        String path = validator.isEmpty(logo) ? qrCodeLogo : logo;

        return validator.isEmpty(path) ? null : storages.get(Storages.TYPE_DISK).getAbsolutePath(path);
    }

    @Override
    public JSONObject prepayApp(String key, String user, String subject, int amount, String billNo, String notice) {
        Map<String, String> map = prepay(key, user, null, subject, amount, billNo, notice, "APP", new HashMap<>());
        if (map == null)
            return null;

        Map<String, String> param = new HashMap<>();
        param.put("appid", map.get("appid"));
        param.put("partnerid", map.get("mch_id"));
        param.put("prepayid", map.get("prepay_id"));
        param.put("package", "Sign=WXPay");
        param.put("noncestr", generator.random(32));
        param.put("timestamp", numeric.toString(System.currentTimeMillis() / 1000, "0"));

        JSONObject object = new JSONObject();
        object.putAll(param);
        object.put("sign", sign(param, findByKey(key).getMchKey()));

        return object;
    }

    @Override
    public JSONObject prepayMini(String key, String user, String openId, String subject, int amount, String billNo, String notice) {
        Map<String, String> map = prepay(key, user, openId, subject, amount, billNo, notice, "JSAPI", new HashMap<>());
        if (map == null)
            return null;

        Map<String, String> param = new HashMap<>();
        param.put("appId", map.get("appid"));
        param.put("timeStamp", numeric.toString(System.currentTimeMillis() / 1000, "0"));
        param.put("nonceStr", generator.random(32));
        param.put("package", "prepay_id=" + map.get("prepay_id"));
        param.put("signType", "MD5");

        JSONObject object = new JSONObject();
        object.putAll(param);
        object.put("sign", sign(param, findByKey(key).getMchKey()));

        return object;
    }

    private Map<String, String> prepay(String key, String user, String openId, String subject, int amount, String billNo, String notice,
                                       String type, Map<String, String> map) {
        WeixinModel weixin = findByKey(key);
        if (weixin == null)
            return null;

        String orderNo = paymentHelper.create("weixin", weixin.getAppId(), user, amount, billNo, notice, map);
        if (validator.isEmpty(orderNo))
            return null;

        map.put("appid", weixin.getAppId());
        map.put("mch_id", weixin.getMchId());
        map.put("nonce_str", generator.random(32));
        map.put("body", subject);
        map.put("out_trade_no", orderNo);
        map.put("total_fee", numeric.toString(amount, "0"));
        map.put("spbill_create_ip", header.getIp());
        map.put("notify_url", root + "/weixin/notice");
        map.put("trade_type", type);
        if (type.equals("JSAPI"))
            map.put("openid", openId);
        map.put("sign", sign(map, weixin.getMchKey()));

        StringBuilder xml = new StringBuilder("<xml>");
        map.forEach((name, value) -> xml.append('<').append(name).append("><![CDATA[").append(value).append("]]></").append(name).append('>'));
        xml.append("</xml>");
        String html = http.post("https://api.mch.weixin.qq.com/pay/unifiedorder", null, xml.toString());
        if (validator.isEmpty(html)) {
            logger.warn(null, "微信预支付[{}:{}]失败！", xml, map);

            return null;
        }

        map = this.xml.toMap(html, false);
        if (!"SUCCESS".equals(map.get("return_code")) || !"SUCCESS".equals(map.get("result_code"))) {
            logger.warn(null, "微信预支付[{}:{}]失败！", xml, map);

            return null;
        }

        return map;
    }

    @Override
    public boolean notice(String appId, String orderNo, String tradeNo, String amount, String returnCode, String resultCode,
                          Map<String, String> map) {
        if (logger.isDebugEnable())
            logger.debug("微信支付结果回调[{}]。", converter.toString(map));

        WeixinModel weixin = findByAppId(appId);
        if (weixin == null)
            return false;

        if (!"SUCCESS".equals(map.get("return_code")) || !"SUCCESS".equals(map.get("result_code"))) {
            logger.warn(null, "微信支付回调[{}]返回失败！", converter.toString(map));

            return false;
        }

        String sign = map.remove("sign");
        if (!sign(map, weixin.getMchKey()).equals(sign)) {
            logger.warn(null, "微信支付回调签名认证[{}]失败！", converter.toString(map));

            return false;
        }

        return orderNo.equals(paymentHelper.complete(orderNo, numeric.toInt(amount), tradeNo, 1, map));
    }

    private String sign(Map<String, String> map, String mchKey) {
        List<String> list = new ArrayList<>(map.keySet());
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        list.forEach(key -> sb.append(key).append('=').append(map.get(key)).append('&'));
        sb.append("key=").append(mchKey);

        return digest.md5(sb.toString()).toUpperCase();
    }

    @Override
    public JSONObject decryptAesCbcPkcs7(String iv, String message) {
        if (validator.isEmpty(iv) || validator.isEmpty(message))
            return new JSONObject();

        String sessionKey = session.get(SESSION_MINI_SESSION_KEY);
        if (validator.isEmpty(sessionKey))
            return new JSONObject();

        try {
            if (Security.getProvider("BC") == null)
                Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(coder.decodeBase64(sessionKey), "AES"),
                    new IvParameterSpec(coder.decodeBase64(iv)));

            return json.toObject(new String(cipher.doFinal(coder.decodeBase64(message))), false);
        } catch (Exception e) {
            logger.warn(e, "解密微信数据[{}:{}:{}]时发生异常！", sessionKey, iv, message);

            return new JSONObject();
        }
    }

    @Override
    public int getContextRefreshedSort() {
        return 24;
    }

    @Override
    public void onContextRefreshed() {
        executeHourJob();
    }

    @Override
    public void executeHourJob() {
        if (!auto || !validator.isEmpty(synchUrl))
            return;

        List<WeixinModel> list = weixinDao.query().getList();
        weixinDao.close();
        list.forEach(this::update);
    }

    private void update(WeixinModel weixin) {
        JSONObject object = json.toObject(http.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                + weixin.getAppId() + "&secret=" + weixin.getSecret(), null, ""));
        if (object == null || !object.containsKey("access_token")) {
            logger.warn(null, "获取微信公众号Token[{}]失败！", object);

            return;
        }

        weixin.setAccessToken(object.getString("access_token"));
        object = json.toObject(http.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token="
                + weixin.getAccessToken(), null, ""));
        if (object != null && object.containsKey("ticket"))
            weixin.setJsapiTicket(object.getString("ticket"));
        else
            logger.warn(null, "获取微信公众号JSAPI Ticket[{}]失败！", object);

        weixin.setTime(dateTime.now());
        weixinDao.save(weixin);
        weixinDao.close();

        if (logger.isInfoEnable())
            logger.info("更新微信公众号[{}]Access Token[{}]与Jsapi Ticket[{}]。",
                    weixin.getAppId(), weixin.getAccessToken(), weixin.getJsapiTicket());
    }

    @Override
    public void executeMinuteJob() {
        if (!auto || validator.isEmpty(synchUrl) || Calendar.getInstance().get(Calendar.MINUTE) % 5 > 0)
            return;

        Map<String, String> parameter = new HashMap<>();
        sign.put(parameter, synchKey);
        JSONObject object = json.toObject(http.get(synchUrl + "/weixin/query", null, parameter));
        if (object == null)
            return;

        JSONArray array = object.getJSONArray("data");
        if (validator.isEmpty(array))
            return;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject obj = array.getJSONObject(i);
            WeixinModel weixin = weixinDao.findByKey(obj.getString("key"));
            if (weixin == null) {
                weixin = new WeixinModel();
                weixin.setKey(obj.getString("key"));
            }
            weixin.setName(obj.getString("name"));
            weixin.setAppId(obj.getString("appId"));
            weixin.setSecret(obj.getString("secret"));
            weixin.setToken(obj.getString("token"));
            weixin.setMchId(obj.getString("mchId"));
            weixin.setMchKey(obj.getString("mchKey"));
            weixin.setAccessToken(obj.getString("accessToken"));
            weixin.setJsapiTicket(obj.getString("jsapiTicket"));
            weixin.setTime(dateTime.now());
            weixinDao.save(weixin);
        }
    }
}
