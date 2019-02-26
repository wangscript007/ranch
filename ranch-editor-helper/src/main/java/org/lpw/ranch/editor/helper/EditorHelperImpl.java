package org.lpw.ranch.editor.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
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
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private Carousel carousel;
    @Value("${ranch.editor.key:ranch.editor}")
    private String key;
    private String findKey;
    private String saveKey;
    private String pdfKey;
    private String copyKey;

    @Override
    public JSONObject find(String id) {
        return find(null, id);
    }

    @Override
    public JSONObject find(Map<String, String> header, String id) {
        if (findKey == null)
            findKey = key + ".find";
        Map<String, String> map = new HashMap<>();
        map.put("id", id);

        return carousel.service(findKey, header, map, false, JSONObject.class);
    }

    @Override
    public JSONObject save(String id, String type, String name, String keyword, int width, int height, String image, Map<String, String> map) {
        return save(null, id, type, name, keyword, width, height, image, map);
    }

    @Override
    public JSONObject save(Map<String, String> header, String id, String type, String name, String keyword, int width, int height,
                           String image, Map<String, String> map) {
        if (map == null)
            map = new HashMap<>();
        if (!validator.isEmpty(id))
            map.put("id", id);
        if (!validator.isEmpty(type))
            map.put("type", type);
        if (!validator.isEmpty(name))
            map.put("name", name);
        if (!validator.isEmpty(keyword))
            map.put("keyword", keyword);
        map.put("width", numeric.toString(width, "0"));
        map.put("height", numeric.toString(height, "0"));
        if (!validator.isEmpty(image))
            map.put("image", image);

        return save(header, map);
    }

    @Override
    public JSONObject save(JSONObject object) {
        return save(null, object);
    }

    @Override
    public JSONObject save(Map<String, String> header, JSONObject object) {
        Map<String, String> map = new HashMap<>();
        object.keySet().forEach(key -> map.put(key, object.getString(key)));

        return save(header, map);
    }

    private JSONObject save(Map<String, String> header, Map<String, String> map) {
        if (saveKey == null)
            saveKey = key + ".save";

        return carousel.service(saveKey, header, map, false, JSONObject.class);
    }

    @Override
    public JSONObject pdf(String id, String email) {
        if (pdfKey == null)
            pdfKey = key + ".pdf";

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("email", email);

        return carousel.service(pdfKey, null, map, false);
    }

    @Override
    public JSONObject copy(String id, String type) {
        if (copyKey == null)
            copyKey = key + ".copy";

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("type", type);

        return carousel.service(copyKey, null, map, false);
    }
}
