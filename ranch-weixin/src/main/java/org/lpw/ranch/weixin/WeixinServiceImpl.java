package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(WeixinModel.NAME + ".service")
public class WeixinServiceImpl implements WeixinService, HourJob, ContextRefreshedListener {
    @Inject
    private Digest digest;
    @Inject
    private Http http;
    @Inject
    private Json json;
    @Inject
    private DateTime dateTime;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WeixinDao weixinDao;
    @Value("${" + WeixinModel.NAME + ".auto:false}")
    private boolean auto;

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
    public void save(WeixinModel weixin) {
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
        if (modify)
            update(model);
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
    public int getContextRefreshedSort() {
        return 24;
    }

    @Override
    public void onContextRefreshed() {
        executeHourJob();
    }

    @Override
    public void executeHourJob() {
        if (auto)
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
}
