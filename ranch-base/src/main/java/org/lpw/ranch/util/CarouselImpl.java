package org.lpw.ranch.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.carousel.CarouselHelper;
import org.lpw.tephra.ctrl.context.Header;
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
    public <T> T service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime, Class<T> jsonClass) {
        String service = carouselHelper.service(key, header == null ? this.header.getMap() : header, parameter, cacheTime);
        if (validator.isEmpty(service))
            return (T) (jsonClass == JSONArray.class ? new JSONArray() : new JSONObject());

        JSONObject object = JSON.parseObject(service);
        if (object.getIntValue("code") != 0)
            return (T) (jsonClass == JSONArray.class ? new JSONArray() : new JSONObject());

        return (T) (jsonClass == JSONArray.class ? object.getJSONArray("data") : object.getJSONObject("data"));
    }
}
