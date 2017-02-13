package org.lpw.ranch.util;

import com.alibaba.fastjson.JSON;
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
    @Value("${ranch.util.carousel.get.cache-time:5}")
    private int cacheTime;

    @Override
    public JSONObject get(String key, String id) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("ids", id);
        JSONObject obj = service(key, null, parameter, cacheTime);
        if (obj.containsKey(id))
            object.putAll(obj.getJSONObject(id));

        return object;
    }

    @Override
    public JSONObject service(String key, Map<String, String> parameter) {
        return service(key, null, parameter, 0);
    }

    @Override
    public JSONObject service(String key, Map<String, String> header, Map<String, String> parameter, int cacheTime) {
        String service = carouselHelper.service(key, header == null ? this.header.getMap() : header, parameter, cacheTime);
        if (validator.isEmpty(service))
            return new JSONObject();

        JSONObject object = JSON.parseObject(service);
        if (object.getIntValue("code") != 0)
            return new JSONObject();

        return object.getJSONObject("data");
    }
}
