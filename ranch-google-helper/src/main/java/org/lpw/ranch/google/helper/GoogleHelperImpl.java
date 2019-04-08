package org.lpw.ranch.google.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Component("ranch.google.helper")
public class GoogleHelperImpl implements GoogleHelper {
    @Inject
    private Carousel carousel;
    @Value("${ranch.google.key:ranch.google}")
    private String key;

    @Override
    public JSONObject auth(String key, String token) {
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("token", token);

        return carousel.service(this.key + ".auth", null, map, false, JSONObject.class);
    }
}
