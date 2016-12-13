package org.lpw.ranch.util;

import net.sf.json.JSONObject;
import org.lpw.tephra.carousel.CarouselHelper;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Component("ranch.util.carousel")
public class CarouselImpl implements Carousel {
    @Autowired
    protected Validator validator;
    @Autowired
    protected CarouselHelper carouselHelper;
    @Value("${ranch.util.carousel.key.user:ranch.user}")
    protected String userKey;

    @Override
    public JSONObject get(String key, String id) {
        JSONObject object = new JSONObject();
        object.put("id", id);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("id", id);
        String service = carouselHelper.service(key, null, parameter, true);
        if (validator.isEmpty(service))
            return object;

        JSONObject json = JSONObject.fromObject(service);
        if (json.getInt("code") != 0)
            return object;

        JSONObject obj = json.getJSONObject("data");
        if (obj.has(id))
            object.putAll(obj.getJSONObject(id));

        return object;
    }

    @Override
    public JSONObject getUser(String id) {
        return get(userKey + ".get", id);
    }
}
