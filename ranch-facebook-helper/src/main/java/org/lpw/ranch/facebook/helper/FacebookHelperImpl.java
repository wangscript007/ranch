package org.lpw.ranch.facebook.helper;

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
@Service("ranch.facebook.helper")
public class FacebookHelperImpl implements FacebookHelper {
    @Inject
    private Carousel carousel;
    @Value("${ranch.facebook.key:ranch.facebook}")
    private String key;

    @Override
    public JSONObject auth(String key, String code) {
        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("code", code);

        return carousel.service(this.key + ".auth", null, map, false, JSONObject.class);
    }
}
