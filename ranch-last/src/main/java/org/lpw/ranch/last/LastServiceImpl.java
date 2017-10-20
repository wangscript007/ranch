package org.lpw.ranch.last;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Service(LastModel.NAME + ".service")
public class LastServiceImpl implements LastService {
    private static final String CACHE_USER_TYPE = LastModel.NAME + ".service.user-type:";

    @Inject
    private Cache cache;
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private Json json;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private LastDao lastDao;
    private Set<String> ignores;

    @Override
    public JSONObject find(String type) {
        return find(userHelper.id(), type, null);
    }

    private JSONObject find(String user, String type, LastModel last) {
        String cacheKey = CACHE_USER_TYPE + user + type;
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, object = getJson(last == null ? lastDao.find(user, type) : last), false);

        return object;
    }

    private JSONObject getJson(LastModel last) {
        if (last == null)
            return new JSONObject();

        JSONObject object = modelHelper.toJson(last);
        if(!validator.isEmpty(last.getJson())){
            JSONObject json = this.json.toObject(last.getJson());
            if (json != null)
                object.putAll(json);
        }

        return object;
    }

    @Override
    public JSONObject save(String type, Map<String, String> map) {
        String user = userHelper.id();
        LastModel last = lastDao.find(user, type);
        if (last == null) {
            last = new LastModel();
            last.setUser(user);
            last.setType(type);
        }
        JSONObject json = new JSONObject();
        map.forEach((key, value) -> {
            if (ignore(key))
                return;

            json.put(key, value);
        });
        last.setJson(json.toJSONString());
        last.setTime(System.currentTimeMillis());
        lastDao.save(last);
        cache.remove(CACHE_USER_TYPE + user + type);

        return find(user, type, last);
    }

    private boolean ignore(String key) {
        if (ignores == null) {
            ignores = new HashSet<>();
            ignores.add("type");
        }

        return ignores.contains(key);
    }
}
