package org.lpw.ranch.last.helper;

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
@Service("ranch.last.helper")
public class LastHelperImpl implements LastHelper {
    @Inject
    private Carousel carousel;
    @Value("${ranch.last.key:ranch.last}")
    private String lastKey;

    @Override
    public JSONObject find(String type) {
        return service(".find", type, null);
    }

    @Override
    public JSONObject save(String type, Map<String, String> map) {
        return service(".save", type, map);
    }

    private JSONObject service(String key, String type, Map<String, String> map) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("type", type);
        if (map != null)
            parameter.putAll(map);

        return carousel.service(lastKey + key, null, parameter, false, JSONObject.class);
    }
}
