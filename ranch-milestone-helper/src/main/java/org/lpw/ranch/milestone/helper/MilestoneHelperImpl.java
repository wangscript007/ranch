package org.lpw.ranch.milestone.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Service("ranch.milestone.helper")
public class MilestoneHelperImpl implements MilestoneHelper {
    @Inject
    private Carousel carousel;
    @Value("${ranch.milestone.key:ranch.milestone}")
    private String key;
    private String findKey;
    private String createKey;

    @Override
    public JSONObject find(String type) {
        if (findKey == null)
            findKey = key + ".find";
        Map<String, String> map = new HashMap<>();
        map.put("type", type);

        return carousel.service(findKey, null, map, false, JSONObject.class);
    }

    @Override
    public JSONObject create(String user, String type, Map<String, String> map) {
        if (createKey == null)
            createKey = key + ".create";
        if (map == null)
            map = new HashMap<>();
        map.put("user", user);
        map.put("type", type);

        return carousel.service(createKey, null, map, false, JSONObject.class);
    }
}
