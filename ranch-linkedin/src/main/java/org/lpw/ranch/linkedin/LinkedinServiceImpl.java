package org.lpw.ranch.linkedin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(LinkedinModel.NAME + ".service")
public class LinkedinServiceImpl implements LinkedinService {
    private static final String CACHE_KEY = LinkedinModel.NAME + ".key:";

    @Inject
    private Cache cache;
    @Inject
    private Http http;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private LinkedinDao linkedinDao;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(linkedinDao.query().getList());
    }

    @Override
    public LinkedinModel findByKey(String key) {
        String cacheKey = CACHE_KEY + key;
        LinkedinModel linkedin = cache.get(cacheKey);
        if (linkedin == null)
            cache.put(cacheKey, linkedin = linkedinDao.findByKey(key), false);

        return linkedin;
    }

    @Override
    public void save(LinkedinModel linkedin) {
        LinkedinModel model = findByKey(linkedin.getKey());
        linkedin.setId(model == null ? null : model.getId());
        linkedinDao.save(linkedin);
        cache.remove(CACHE_KEY + linkedin.getKey());
    }

    @Override
    public void delete(String id) {
        LinkedinModel linkedin = linkedinDao.findById(id);
        if (linkedin == null)
            return;

        linkedinDao.delete(id);
        cache.remove(CACHE_KEY + linkedin.getKey());
    }

    @Override
    public JSONObject auth(String key, String code, String redirectUri) {
        LinkedinModel linkedin = findByKey(key);
        Map<String, String> map = new HashMap<>();
        map.put("grant_type", "authorization_code");
        map.put("code", code);
        map.put("redirect_uri", redirectUri);
        map.put("client_id", linkedin.getAppId());
        map.put("client_secret", linkedin.getSecret());
        String string = http.post("https://www.linkedin.com/uas/oauth2/accessToken", null, map);
        JSONObject object = json.toObject(string);
        if (object == null || !object.containsKey("access_token")) {
            logger.warn(null, "获取Linkedin认证信息[{}:{}]失败！", map, string);

            return new JSONObject();
        }

        map.clear();
        map.put("Authorization", "Bearer " + object.getString("access_token"));
        string = http.get("https://api.linkedin.com/v1/people/~:(id,formatted-name,picture-url)?format=json", map, "");
        JSONObject people = json.toObject(string);
        if (people == null)
            logger.warn(null, "获取Linkedin用户信息[{}:{}]失败！", map, string);
        else
            object.putAll(people);

        return object;
    }
}
