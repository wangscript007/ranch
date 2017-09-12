package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.storage.Storages;
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

import javax.inject.Inject;
import java.io.OutputStream;
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
    private Logger logger;
    @Inject
    private Storages storages;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Header header;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private WeixinDao weixinDao;
    @Value("${tephra.ctrl.service-root:}")
    private String root;
    @Value("${" + WeixinModel.NAME + ".qr-code.size:256}")
    private int qrCodeSize;
    @Value("${" + WeixinModel.NAME + ".qr-code.logo:}")
    private String qrCodeLogo;
    @Value("${" + WeixinModel.NAME + ".auto:false}")
    private boolean auto;
    @Value("${" + WeixinModel.NAME + ".synch.url=}")
    private String synchUrl;
    @Value("${" + WeixinModel.NAME + ".synch.key=}")
    private String synchKey;
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
            logger.info("验证微信服务[{}:signature={};timestamp={};nonce={};echostr={}]。", success, signature, timestamp, nonce, echostr);

        return success ? echostr : "failure";
    }

    @Override
    public JSONObject auth(String key, String code) {
        JSONObject object = new JSONObject();
        Map<String, String> map = new HashMap<>();
        WeixinModel weixin = weixinDao.findByKey(key);
        map.put("appid", weixin.getAppId());
        map.put("secret", weixin.getSecret());
        map.put("code", code);
        map.put("grant_type", "authorization_code");
        JSONObject obj = json.toObject(http.get("https://api.weixin.qq.com/sns/oauth2/access_token", null, map));
        if (obj == null || !obj.containsKey("openid"))
            return object;

        object.putAll(obj);
        String openId = obj.getString("openid");
        if (obj.containsKey("access_token")) {
            map.clear();
            map.put("access_token", obj.getString("access_token"));
            map.put("openid", openId);
            map.put("lang", "zh_CN");
            obj = json.toObject(http.get("https://api.weixin.qq.com/sns/userinfo", null, map));
            if (obj != null)
                object.putAll(obj);
        }

        if (logger.isDebugEnable())
            logger.debug("获得微信用户认证信息[{}:{}]。", code, object);

        return object;
    }

    @Override
    public void prepayQrCode(String key, String user, String subject, int amount, String notice, int size, String logo, OutputStream outputStream) {
        Map<String, String> map = prepay(key, user, subject, amount, notice, "NATIVE", new HashMap<>());
        if (map == null)
            return;

        qrCode.create(map.get("code_url"), size > 0 ? size : qrCodeSize, getLogo(logo), outputStream);
    }

    @Override
    public String prepayQrCodeBase64(String key, String user, String subject, int amount, String notice, int size, String logo) {
        Map<String, String> map = prepay(key, user, subject, amount, notice, "NATIVE", new HashMap<>());

        return map == null ? null : qrCode.create(map.get("code_url"), size > 0 ? size : qrCodeSize, getLogo(logo));
    }

    private String getLogo(String logo) {
        String path = validator.isEmpty(logo) ? qrCodeLogo : logo;

        return validator.isEmpty(path) ? null : storages.get(Storages.TYPE_DISK).getAbsolutePath(path);
    }

    @Override
    public JSONObject prepayApp(String key, String user, String subject, int amount, String notice) {
        Map<String, String> map = prepay(key, user, subject, amount, notice, "APP", new HashMap<>());
        if (map == null)
            return null;

        Map<String, String> param = new HashMap<>();
        param.put("appid", map.get("appid"));
        param.put("partnerid", map.get("mch_id"));
        param.put("prepayid", map.get("prepay_id"));
        param.put("package", "Sign=WXPay");
        param.put("noncestr", generator.random(32));
        param.put("timestamp", converter.toString(System.currentTimeMillis() / 1000, "0"));

        JSONObject object = new JSONObject();
        object.putAll(param);
        object.put("sign", sign(param, findByKey(key).getMchKey()));

        return object;
    }

    private Map<String, String> prepay(String key, String user, String subject, int amount, String notice, String type, Map<String, String> map) {
        WeixinModel weixin = findByKey(key);
        if (weixin == null)
            return null;

        String orderNo = paymentHelper.create("weixin", user, amount, notice);
        if (validator.isEmpty(orderNo))
            return null;

        map.put("appid", weixin.getAppId());
        map.put("mch_id", weixin.getMchId());
        map.put("nonce_str", generator.random(32));
        map.put("body", subject);
        map.put("out_trade_no", orderNo);
        map.put("total_fee", converter.toString(amount, "0"));
        map.put("spbill_create_ip", header.getIp());
        map.put("notify_url", root + "/weixin/notice");
        map.put("trade_type", type);
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
    public boolean notice(String appId, String orderNo, String tradeNo, String amount, String returnCode, String resultCode, Map<String, String> map) {
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

        return orderNo.equals(paymentHelper.complete(orderNo, numeric.toInt(amount), tradeNo, 1));
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
    public int getContextRefreshedSort() {
        return 24;
    }

    @Override
    public void onContextRefreshed() {
        executeHourJob();
    }

    @Override
    public void executeHourJob() {
        if (auto && validator.isEmpty(synchUrl))
            weixinDao.query().getList().forEach(this::update);
    }

    private void update(WeixinModel weixin) {
        JSONObject object = json.toObject(http.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + weixin.getAppId() + "&secret=" + weixin.getSecret(), null, ""));
        if (object == null || !object.containsKey("access_token")) {
            logger.warn(null, "获取微信公众号Token[{}]失败！", object);

            return;
        }

        weixin.setAccessToken(object.getString("access_token"));
        object = json.toObject(http.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + weixin.getAccessToken(), null, ""));
        if (object != null && object.containsKey("ticket"))
            weixin.setJsapiTicket(object.getString("ticket"));
        else
            logger.warn(null, "获取微信公众号JSAPI Ticket[{}]失败！", object);

        weixin.setTime(dateTime.now());
        weixinDao.save(weixin);

        if (logger.isInfoEnable())
            logger.info("更新微信公众号[{}]Access Token[{}]与Jsapi Ticket[{}]。", weixin.getAppId(), weixin.getAccessToken(), weixin.getJsapiTicket());
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

        List<WeixinModel> list = modelHelper.fromJson(object.getJSONArray("data"), WeixinModel.class);
        if (validator.isEmpty(list))
            return;

        list.forEach(this::save);
    }
}
