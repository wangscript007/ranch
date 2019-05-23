package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.weixin.info.InfoService;
import org.lpw.ranch.weixin.reply.ReplyService;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.ctrl.http.ServiceHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Coder;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.QrCode;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.util.Xml;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.Security;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lpw
 */
@Service(WeixinModel.NAME + ".service")
public class WeixinServiceImpl implements WeixinService, ContextRefreshedListener, HourJob, MinuteJob {
    private static final String CACHE_TICKET_SESSION_ID = WeixinModel.NAME + ".ticket-session-id:";
    private static final String SESSION_SUBSCRIBE_SIGN_IN = WeixinModel.NAME + ".subscribe-sign-in";
    private static final String SESSION_MINI = WeixinModel.NAME + ".mini";
    private static final String SESSION_MINI_SESSION_KEY = WeixinModel.NAME + ".mini.session-key";

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
    private Io io;
    @Inject
    private Xml xml;
    @Inject
    private Sign sign;
    @Inject
    private QrCode qrCode;
    @Inject
    private Coder coder;
    @Inject
    private Context context;
    @Inject
    private Cache cache;
    @Inject
    private Logger logger;
    @Inject
    private Storages storages;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private Header header;
    @Inject
    private Session session;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private InfoService infoService;
    @Inject
    private ReplyService replyService;
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
            refreshAccessToken(model);

        return modelHelper.toJson(model);
    }

    @Override
    public void refreshAccessToken(String key) {
        refreshAccessToken(findByKey(key));
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
    public void notice(String appId, String string) {
        WeixinModel weixin = weixinDao.findByAppId(appId);
        if (weixin == null) {
            logger.warn(null, "无法获得微信[{}:{}]配置！", appId, string);

            return;
        }

        String msgType = getValue(string, "<MsgType><![CDATA[", "]]></MsgType>");
        if (msgType == null) {
            logger.warn(null, "无法获得微信消息类型[{}]信息！", string);

            return;
        }

        if (msgType.equals("event"))
            event(weixin, string);
        else if (msgType.equals("text"))
            replyService.send(weixin, getOpenId(string), "text", getValue(string, "<Content><![CDATA[", "]]></Content>"));
    }

    private void event(WeixinModel weixin, String string) {
        String event = getValue(string, "<Event><![CDATA[", "]]></Event>");
        if (event == null) {
            logger.warn(null, "无法获得微信事件类型[{}]信息！", string);

            return;
        }

        if (event.equals("subscribe") || event.equals("SCAN"))
            signIn(weixin, string, event);
    }

    private void signIn(WeixinModel weixin, String string, String event) {
        String openId = getOpenId(string);
        if (openId == null) {
            logger.warn(null, "无法获得微信消息[{}]中Open ID信息！", string);

            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("openid", openId);
        JSONObject object = byAccessToken(weixin, accessToken -> {
            map.put("access_token", weixin.getAccessToken());
            String str = http.get("https://api.weixin.qq.com/cgi-bin/user/info", null, map);
            if (logger.isInfoEnable())
                logger.info("获取微信用户信息[{}:{}]。", map, str);

            return str;
        });
        if (object == null || object.containsKey("errcode") || object.size() <= 2) {
            logger.warn(null, "获取微信[{}]用户[{}:{}]信息失败！", weixin.getKey(), map, object);

            return;
        }

        if (object.containsKey("unionid"))
            infoService.save(weixin.getKey(), weixin.getAppId(), object.getString("unionid"), openId);

        String ticket = getValue(string, "<Ticket><![CDATA[", "]]></Ticket>");
        if (ticket == null) {
            logger.warn(null, "无法获得微信消息[{}]中Ticket信息！", string);

            return;
        }
        String sessionId = cache.get(CACHE_TICKET_SESSION_ID + ticket);
        if (validator.isEmpty(sessionId)) {
            logger.warn(null, "无法获得微信消息Ticket[{}]缓存的Session ID！", ticket);

            return;
        }
        session.set(sessionId, SESSION_SUBSCRIBE_SIGN_IN, object);
        replyService.send(weixin, openId, "event", event);
    }

    private String getOpenId(String string) {
        return getValue(string, "<FromUserName><![CDATA[", "]]></FromUserName>");
    }

    private String getValue(String string, String prefix, String suffix) {
        int prefixIndexOf = string.indexOf(prefix);
        int suffixIndexOf = string.indexOf(suffix);

        return prefixIndexOf == -1 || suffixIndexOf == -1 ? null : string.substring(prefixIndexOf + prefix.length(), suffixIndexOf);
    }

    @Override
    public String subscribeQr(String key) {
        if (validator.isEmpty(synchUrl)) {
            WeixinModel weixin = weixinDao.findByKey(key);
            JSONObject object = new JSONObject();
            object.put("expire_seconds", 3600);
            object.put("action_name", "QR_STR_SCENE");
            JSONObject info = new JSONObject();
            info.put("scene_str", "sign-in-sid:" + session.getId());
            object.put("action_info", info);
            JSONObject obj = byAccessToken(weixin, accessToken -> http.post(
                    "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken,
                    null, object.toJSONString()));
            if (obj == null || !obj.containsKey("ticket") || !obj.containsKey("url")) {
                logger.warn(null, "获取微信关注二维码[{}:{}:{}]信息失败！", weixin.getAccessToken(), object, obj);

                return null;
            }

            cache.put(CACHE_TICKET_SESSION_ID + obj.getString("ticket"), session.getId(), false);

            return obj.getString("url");
        }

        Map<String, String> header = new HashMap<>();
        header.put(ServiceHelper.SESSION_ID, session.getId());
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        String string = http.post(synchUrl + "/weixin/subscribe-qr", header, parameter);
        JSONObject object = json.toObject(string);
        if (object == null || object.getIntValue("code") > 0) {
            logger.warn(null, "获取微信关注二维码[{}:{}:{}]信息失败！", synchUrl, key, string);

            return null;
        }

        return object.getString("data");
    }

    @Override
    public JSONObject subscribeSignIn() {
        JSONObject object = session.get(SESSION_SUBSCRIBE_SIGN_IN);
        if (object != null)
            return object;

        if (validator.isEmpty(synchUrl))
            return new JSONObject();

        Map<String, String> map = new HashMap<>();
        map.put(ServiceHelper.SESSION_ID, session.getId());
        String string = http.post(synchUrl + "/weixin/subscribe-sign-in", map, "");
        object = json.toObject(string);
        if (object == null || object.getIntValue("code") > 0) {
            logger.warn(null, "获取微信关注登入[{}:{}:{}]同步信息失败！", synchUrl, session.getId(), string);

            return new JSONObject();
        }

        JSONObject data = object.getJSONObject("data");
        if (!data.isEmpty())
            session.set(SESSION_SUBSCRIBE_SIGN_IN, data);

        return data;
    }

    @Override
    public JSONObject auth(String key, String code) {
        if (code.equals("subscribe-sign-in")) {
            JSONObject object = session.get(SESSION_SUBSCRIBE_SIGN_IN);

            return object == null ? new JSONObject() : object;
        }

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
    public JSONObject auth(String key, String code, String iv, String message, String iv2, String message2) {
        WeixinModel weixin = weixinDao.findByKey(key);
        JSONObject object = code.equals("decrypt-iv-message") ? session.get(SESSION_MINI) : jscode2session(weixin, code);
        if (object.isEmpty())
            return object;

        if (validator.isEmpty(iv) || validator.isEmpty(message)) {
            infoService.save(key, weixin.getAppId(), object.getString("unionid"), object.getString("openid"));

            return object;
        }

        String sessionKey = session.get(SESSION_MINI_SESSION_KEY);
        object.putAll(decryptAesCbcPkcs7(sessionKey, iv, message));
        if (!validator.isEmpty(iv2) && !validator.isEmpty(message2))
            object.putAll(decryptAesCbcPkcs7(sessionKey, iv2, message2));
        infoService.save(key, weixin.getAppId(), object.getString("unionid"), object.getString("openid"));

        if (logger.isDebugEnable())
            logger.debug("获得微信小程序用户认证信息[{}:{}:{}]。", key, code, object);

        return object;
    }

    private JSONObject jscode2session(WeixinModel weixin, String code) {
        Map<String, String> map = getAuthMap(weixin);
        map.put("js_code", code);
        String string = http.get("https://api.weixin.qq.com/sns/jscode2session", null, map);
        JSONObject object = json.toObject(string);
        if (object == null || !object.containsKey("openid")) {
            logger.warn(null, "获取微信小程序用户认证信息[{}:{}:{}]失败！", weixin.getKey(), map, string);

            return new JSONObject();
        }

        session.set(SESSION_MINI, object);
        session.set(SESSION_MINI_SESSION_KEY, object.getString("session_key"));
        object.remove("session_key");
        if (logger.isDebugEnable())
            logger.debug("获得微信小程序用户认证信息[{}:{}:{}]。", weixin.getKey(), map, object);

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
        if (weixin == null) {
            logger.warn(null, "获取微信配置[{}]失败！", key);

            return null;
        }

        String orderNo = paymentHelper.create("weixin", weixin.getAppId(), user, amount, billNo, notice, map);
        if (validator.isEmpty(orderNo)) {
            logger.warn(null, "创建支付订单[{}:{}:{}:{}:{}:{}]失败！", weixin.getAppId(), user, amount, billNo, notice, map);

            return null;
        }

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

        if (!sign(map, weixin.getMchKey()).equals(map.get("sign"))) {
            logger.warn(null, "微信支付回调签名认证[{}]失败！", converter.toString(map));

            return false;
        }

        return orderNo.equals(paymentHelper.complete(orderNo, numeric.toInt(amount), tradeNo, 1, map));
    }

    private String sign(Map<String, String> map, String mchKey) {
        List<String> list = new ArrayList<>(map.keySet());
        list.remove("sign");
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        list.forEach(key -> sb.append(key).append('=').append(map.get(key)).append('&'));
        sb.append("key=").append(mchKey);

        return digest.md5(sb.toString()).toUpperCase();
    }

    @Override
    public JSONObject decryptAesCbcPkcs7(String key, String code, String iv, String message) {
        if (!validator.isEmpty(key) && !validator.isEmpty(code))
            jscode2session(findByKey(key), code);
        String sessionKey = session.get(SESSION_MINI_SESSION_KEY);

        return validator.isEmpty(sessionKey) ? new JSONObject() : decryptAesCbcPkcs7(sessionKey, iv, message);
    }

    private JSONObject decryptAesCbcPkcs7(String sessionKey, String iv, String message) {
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
    public String wxaCodeUnlimit(String key, String scene, String page, int width, boolean autoColor, JSONObject lineColor, boolean hyaline) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        JSONObject object = new JSONObject();
        object.put("scene", scene);
        object.put("page", page);
        object.put("width", width);
        object.put("auto_color", autoColor);
        object.put("line_color", lineColor);
        object.put("is_hyaline", hyaline);
        Map<String, String> responseHeaders = new HashMap<>();
        File file = new File(context.getAbsoluteRoot() + "/" + generator.random(32) + ".jpg");
        try {
            OutputStream outputStream = new FileOutputStream(file);
            http.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token="
                    + findByKey(key).getAccessToken(), headers, object.toJSONString(), null, responseHeaders, outputStream);
            outputStream.close();
            if ("image/jpeg".equals(responseHeaders.get("Content-Type")))
                return wormholeHelper.image(null, null, null, file);
            else {
                String failure = io.readAsString(file.getAbsolutePath());
                logger.warn(null, "获取微信二维码[{}:{}:{}]失败！", object, converter.toString(responseHeaders), failure);

                return failure;
            }
        } catch (Throwable throwable) {
            logger.warn(throwable, "获取微信二维码[{}:{}:{}]时发生异常！", object,
                    converter.toString(responseHeaders), io.readAsString(file.getAbsolutePath()));

            return "";
        } finally {
            io.delete(file);
        }
    }

    @Override
    public JSONObject jsapiTicketSignature(String key, JSONObject param) {
        if (validator.isEmpty(param))
            return null;

        WeixinModel weixin = findByKey(key);
        param.put("appid", weixin.getAppId());
        param.put("noncestr", generator.random(32));
        param.put("jsapi_ticket", weixin.getJsapiTicket());
        param.put("timestamp", System.currentTimeMillis() / 1000);
        List<String> list = new ArrayList<>(param.keySet());
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        list.forEach(k -> sb.append('&').append(k).append('=').append(param.get(k)));
        param.remove("jsapi_ticket");
        param.put("signature", digest.sha1(sb.deleteCharAt(0).toString()));

        return param;
    }

    @Override
    public JSONObject sendTemplateMessage(String key, String receiver, String templateId, String url, String miniAppId, String miniPagePath,
                                          JSONObject data, String color) {
        WeixinModel weixin = findByKey(key);
        if (weixin == null)
            return new JSONObject();

        JSONObject object = new JSONObject();
        String openId = infoService.findOpenId(weixin.getAppId(), receiver);
        object.put("touser", openId == null ? receiver : openId);
        object.put("template_id", templateId);
        if (!validator.isEmpty(url))
            object.put("url", url);
        if (!validator.isEmpty(miniAppId)) {
            JSONObject miniprogram = new JSONObject();
            miniprogram.put("appid", miniAppId);
            miniprogram.put("pagepath", miniPagePath);
            object.put("miniprogram", miniprogram);
        }
        object.put("data", data);
        if (!validator.isEmpty(color))
            object.put("color", color);

        return byAccessToken(weixin, accessToken -> {
            String string = http.post("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="
                    + accessToken, null, object.toJSONString());
            if (logger.isInfoEnable())
                logger.info("发送微信模板消息[{}:{}]。", object.toJSONString(), string);

            return string;
        });
    }

    @Override
    public JSONObject sendMiniTemplateMessage(String key, String receiver, String templateId, String page, String formId,
                                              JSONObject data, String keyword) {
        WeixinModel weixin = findByKey(key);
        if (weixin == null)
            return new JSONObject();

        JSONObject object = new JSONObject();
        String openId = infoService.findOpenId(weixin.getAppId(), receiver);
        object.put("touser", openId == null ? receiver : openId);
        object.put("template_id", templateId);
        object.put("form_id", formId);
        if (!validator.isEmpty(page))
            object.put("page", page);
        object.put("data", data);
        if (!validator.isEmpty(keyword))
            object.put("keyword", keyword);

        return byAccessToken(weixin, accessToken -> {
            String string = http.post("https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="
                    + accessToken, null, object.toJSONString());
            if (logger.isInfoEnable())
                logger.info("发送微信小程序模板消息[{}:{}]。", object.toJSONString(), string);

            return string;
        });
    }

    @Override
    public JSONObject byAccessToken(WeixinModel weixin, Function<String, String> function) {
        String string = function.apply(weixin.getAccessToken());
        JSONObject object = json.toObject(string);
        if (object == null) {
            logger.warn(null, "获取微信Access Token信息[{}:{}:{}]失败！", weixin.getKey(), weixin.getAppId(), string);

            return null;
        }

        if (object.containsKey("errcode") && object.getIntValue("errcode") == 42001) {
            refreshAccessToken(weixin);
            string = function.apply(weixin.getAccessToken());
            object = json.toObject(string);
            if (object == null) {
                logger.warn(null, "获取微信Access Token信息[{}:{}:{}]失败！", weixin.getKey(), weixin.getAppId(), string);

                return null;
            }
        }

        return object;
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

        String lockId = lockHelper.lock(WeixinModel.NAME + ".hour", 100, 60);
        if (lockId == null)
            return;

        List<WeixinModel> list = weixinDao.query().getList();
        weixinDao.close();
        list.forEach(this::refreshAccessToken);
        lockHelper.unlock(lockId);
    }

    private void refreshAccessToken(WeixinModel weixin) {
        for (int i = 0; i < 5; i++) {
            try {
                if (refreshAccessTokenOnce(weixin))
                    break;
            } catch (Throwable throwable) {
                logger.warn(throwable, "更新微信Access Token时发生异常！", modelHelper.toJson(weixin));
            }
        }
    }

    private boolean refreshAccessTokenOnce(WeixinModel weixin) {
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "client_credential");
        map.put("appid", weixin.getAppId());
        map.put("secret", weixin.getSecret());
        String string = http.get("https://api.weixin.qq.com/cgi-bin/token", null, map);
        JSONObject object = json.toObject(string);
        if (object == null || !object.containsKey("access_token")) {
            logger.warn(null, "获取微信公众号[{}]Token[{}]失败！", map, string);

            return false;
        }

        weixin.setAccessToken(object.getString("access_token"));
        map.clear();
        map.put("type", "jsapi");
        map.put("access_token", weixin.getAccessToken());
        string = http.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket", null, map);
        object = json.toObject(string);
        if (object != null && object.containsKey("ticket"))
            weixin.setJsapiTicket(object.getString("ticket"));
        else
            logger.warn(null, "获取微信公众号JSAPI Ticket[{}:{}]失败！", modelHelper.toJson(weixin), object);

        weixin.setTime(dateTime.now());
        weixinDao.save(weixin);
        weixinDao.close();

        if (logger.isInfoEnable())
            logger.info("更新微信公众号[{}]Access Token[{}]与Jsapi Ticket[{}]。",
                    weixin.getAppId(), weixin.getAccessToken(), weixin.getJsapiTicket());

        return true;
    }

    @Override
    public void executeMinuteJob() {
        if (!auto || validator.isEmpty(synchUrl))
            return;

        String lockId = lockHelper.lock(WeixinModel.NAME + ".hour", 100, 60);
        if (lockId == null)
            return;

        Map<String, String> parameter = new HashMap<>();
        sign.put(parameter, synchKey);
        JSONObject object = json.toObject(http.get(synchUrl + "/weixin/query", null, parameter));
        if (object == null || !object.containsKey("data")) {
            lockHelper.unlock(lockId);

            return;
        }

        JSONArray array = object.getJSONArray("data");
        if (validator.isEmpty(array)) {
            lockHelper.unlock(lockId);

            return;
        }

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
        lockHelper.unlock(lockId);
    }
}
