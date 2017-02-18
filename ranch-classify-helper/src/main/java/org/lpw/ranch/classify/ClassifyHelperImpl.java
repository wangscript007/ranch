package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.util.ServiceHelperSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.classify.helper")
public class ClassifyHelperImpl extends ServiceHelperSupport implements ClassifyHelper {
    @Value("${ranch.classify.key:ranch.classify}")
    private String key;

    @Override
    public JSONArray list(String key, int size) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("pageSize", "" + size);

        return carousel.service(this.key + ".list", null, parameter, true, JSONArray.class);
    }

    @Override
    protected String getKey() {
        return key;
    }
}
