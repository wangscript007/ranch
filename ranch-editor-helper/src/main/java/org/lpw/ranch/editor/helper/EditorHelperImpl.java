package org.lpw.ranch.editor.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Numeric;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Controller("ranch.editor.helper")
public class EditorHelperImpl implements EditorHelper {
    @Inject
    private Numeric numeric;
    @Inject
    private Carousel carousel;
    @Value("${ranch.editor.key:ranch.editor}")
    private String key;
    private String saveKey;

    @Override
    public JSONObject save(String type, String name, String keyword, int width, int height, String image, Map<String, String> map) {
        if (saveKey == null)
            saveKey = key + ".save";

        if (map == null)
            map = new HashMap<>();
        map.put("type", type);
        map.put("name", name);
        map.put("keyword", keyword);
        map.put("width", numeric.toString(width, "0"));
        map.put("height", numeric.toString(height, "0"));
        map.put("image", image);

        return carousel.service(saveKey, null, map, false, JSONObject.class);
    }
}
