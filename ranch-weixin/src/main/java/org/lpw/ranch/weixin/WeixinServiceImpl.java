package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.HourJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author lpw
 */
@Service(WeixinModel.NAME + ".service")
public class WeixinServiceImpl implements WeixinService, HourJob, ContextRefreshedListener {
    private static final String CACHE_MODEL = WeixinModel.NAME + ".service.model:";

    @Inject
    private Cache cache;
    @Inject
    private Digest digest;
    @Inject
    private Http http;
    @Inject
    private DateTime dateTime;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WeixinDao weixinDao;

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
    public JSONArray query() {
        return modelHelper.toJson(weixinDao.query().getList());
    }

    @Override
    public void save(WeixinModel weixin) {
        WeixinModel model = weixinDao.findByKey(weixin.getKey());
        if (model == null) {
            model = new WeixinModel();
            model.setKey(weixin.getKey());
        }
        model.setName(weixin.getName());
        model.setAppId(weixin.getAppId());
        model.setSecret(weixin.getSecret());
        model.setToken(weixin.getToken());
        model.setMchId(weixin.getMchId());
        model.setMchKey(weixin.getMchKey());
        weixinDao.save(model);
        cache.remove(CACHE_MODEL + weixin.getKey());
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
        weixinDao.query().getList().forEach(weixin -> {
            JSONObject object = JSON.parseObject(http.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + weixin.getAppId() + "&secret=" + weixin.getSecret(), null, ""));
            if (object != null && object.containsKey("access_token")) {
                weixin.setAccessToken(object.getString("access_token"));
                if (logger.isInfoEnable())
                    logger.info("获取微信公众号Token[{}:{}]。", weixin.getAppId(), weixin.getAccessToken());
            } else
                logger.warn(null, "获取微信公众号Token[{}]失败！", object);

            object = JSON.parseObject(http.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi&access_token=" + weixin.getAccessToken(), null, ""));
            if (object != null && object.containsKey("ticket")) {
                weixin.setJsapiTicket(object.getString("ticket"));
                if (logger.isInfoEnable())
                    logger.info("获取微信公众号JSAPI Ticket[{}:{}]。", weixin.getAppId(), weixin.getJsapiTicket());
            } else
                logger.warn(null, "获取微信公众号JSAPI Ticket[{}]失败！", object);

            weixin.setTime(dateTime.now());
            weixinDao.save(weixin);
            cache.remove(CACHE_MODEL + weixin.getKey());
        });
    }
}
