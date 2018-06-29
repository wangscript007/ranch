package org.lpw.ranch.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.carousel.CarouselHelper;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Component("ranch.util.carousel")
public class CarouselImpl implements Carousel {
    @Inject
    private Validator validator;
    @Inject
    private Json json;
    @Inject
    private Sign sign;
    @Inject
    private Logger logger;
    @Inject
    private Header header;
    @Inject
    private CarouselHelper carouselHelper;
    @Value("${ranch.util.carousel.cache-time:5}")
    private int cacheTime;

    @Override
    public JSONObject get(String key, String id) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("ids", id);
        JSONObject data = service(key, null, parameter, true, JSONObject.class);

        return data.containsKey(id) ? data.getJSONObject(id) : object;
    }

    @Override
    public <T> T service(String key, Map<String, String> header, Map<String, String> parameter, boolean cacheable, Class<T> jsonClass) {
        return service(key, header, parameter, cacheable ? cacheTime : 0, jsonClass);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <T> T service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime, Class<T> jsonClass) {
        JSONObject object = service(key, header, parameter, cacheTime);
        if (!object.containsKey("code") || object.getIntValue("code") != 0) {
            logger.warn(null, "执行服务[{}:{}]失败！", key, object.toJSONString());

            return (T) (jsonClass == JSONArray.class ? new JSONArray() : new JSONObject());
        }

        return (T) (jsonClass == JSONArray.class ? object.getJSONArray("data") : object.getJSONObject("data"));
    }

    @Override
    public JSONObject service(String key, Map<String, String> header, Map<String, String> parameter, boolean cacheable) {
        return service(key, header, parameter, cacheable ? cacheTime : 0);
    }

    @Override
    public JSONObject service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime) {
        Map<String, String> map = new HashMap<>();
        put(map, this.header.getMap());
        put(map, header);
        sign.put(parameter, null);
        String service = carouselHelper.service(key, map, parameter, cacheTime);
        if (validator.isEmpty(service))
            return new JSONObject();

        JSONObject object = json.toObject(service);

        return object == null ? new JSONObject() : json.toObject(service);
    }

    private void put(Map<String, String> map, Map<String, String> header) {
        if (!validator.isEmpty(header))
            map.putAll(header);
    }
}
