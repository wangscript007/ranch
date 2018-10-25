package org.lpw.ranch.link.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.link.helper")
public class LinkHelperImpl implements LinkHelper {
    @Inject
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private Carousel carousel;
    @Value("${ranch.link.key:ranch.link}")
    private String key;

    @Override
    public JSONObject query(String type, String id1, String id2, int pageSize, int pageNum) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        if (!validator.isEmpty(id1))
            map.put("id1", id1);
        if (!validator.isEmpty(id2))
            map.put("id2", id2);
        if (pageSize > 0)
            map.put("pageSize", numeric.toString(pageSize, "0"));
        if (pageNum > 0)
            map.put("pageNum", numeric.toString(pageNum, "0"));

        return carousel.service(key + ".query", null, map, false, JSONObject.class);
    }

    @Override
    public int count(String type, String id1, String id2) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id1", id1);
        map.put("id2", id2);

        return carousel.service(key + ".count", null, map, false).getIntValue("data");
    }

    @Override
    public JSONObject find(String type, String id1, String id2) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id1", id1);
        map.put("id2", id2);

        return carousel.service(key + ".find", null, map, false, JSONObject.class);
    }

    @Override
    public JSONObject save(String type, String id1, String id2) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id1", id1);
        map.put("id2", id2);

        return carousel.service(key + ".save", null, map, false, JSONObject.class);
    }

    @Override
    public void delete(String type, String id1, String id2) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("id1", id1);
        map.put("id2", id2);
        carousel.service(key + ".delete", null, map, false);
    }
}
