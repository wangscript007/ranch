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
    private String findSaveKey;

    @Override
    public JSONObject findSave(String user, String type, Map<String, String> map) {
        if (findSaveKey == null)
            findSaveKey = key + ".find-save";
        if (map == null)
            map = new HashMap<>();
        map.put("user", user);
        map.put("type", type);

        return carousel.service(findSaveKey, null, map, false, JSONObject.class);
    }
}
