package org.lpw.ranch.weixin.qrcode;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.WeixinModel;
import org.lpw.ranch.weixin.WeixinService;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(QrcodeModel.NAME + ".service")
public class QrcodeServiceImpl implements QrcodeService {
    @Inject
    private Cache cache;
    @Inject
    private DateTime dateTime;
    @Inject
    private Http http;
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private WeixinService weixinService;
    @Inject
    private QrcodeDao qrcodeDao;

    @Override
    public JSONObject query(String key, String appId, String user, String name, String scene, String[] time) {
        return qrcodeDao.query(key, appId, user, name, scene, dateTime.toTimeRange(time),
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject find(String key, String user, String name) {
        JSONObject object = cache.computeIfAbsent(QrcodeModel.NAME + ".find:" + key + "," + user + "," + name,
                k -> modelHelper.toJson(qrcodeDao.find(key, user, name)), false);

        return object == null ? new JSONObject() : object;
    }

    @Override
    public QrcodeModel create(String key, String user, String name, String scene) {
        WeixinModel weixin = weixinService.findByKey(key);
        if (weixin == null)
            return null;

        JSONObject object = weixinService.byAccessToken(weixin, accessToken -> http.post(
                "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken, null,
                "{\"action_name\": \"QR_LIMIT_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + scene + "\"}}}"));
        if (object == null)
            return null;

        QrcodeModel qrcode = new QrcodeModel();
        qrcode.setKey(key);
        qrcode.setAppId(weixin.getAppId());
        qrcode.setUser(validator.isEmpty(user) ? "" : user);
        qrcode.setName(name);
        qrcode.setScene(scene);
        qrcode.setTicket(object.getString("ticket"));
        qrcode.setUrl(object.getString("url"));
        qrcode.setTime(dateTime.now());
        qrcodeDao.save(qrcode);

        return qrcode;
    }

    @Override
    public void delete(String id) {
        qrcodeDao.delete(id);
    }
}
