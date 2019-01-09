package org.lpw.ranch.linkedin.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.linkedin.helper")
public class LinkedinHelperImpl implements LinkedinHelper {
    @Inject
    private Carousel carousel;
    @Value("${ranch.linkedin.key:ranch.linkedin}")
    private String key;

    @Override
    public JSONObject auth(String key, String code, String redirectUri) {
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("code", code);
        map.put("redirectUri", redirectUri);

        return carousel.service(this.key + ".auth", null, map, false, JSONObject.class);
    }
}
