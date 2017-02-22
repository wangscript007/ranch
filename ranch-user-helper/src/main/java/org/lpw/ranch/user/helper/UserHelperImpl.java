package org.lpw.ranch.user.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.ServiceHelperSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.user.helper")
public class UserHelperImpl extends ServiceHelperSupport implements UserHelper {
    @Value("${ranch.user.key:ranch.user}")
    private String key;
    private String findKey;
    private String signKey;

    @Override
    public JSONObject find(String code) {
        if (findKey == null)
            findKey = key + ".find";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("code", code);

        return carousel.service(findKey, null, parameter, true, JSONObject.class);
    }

    @Override
    public JSONObject sign() {
        if (signKey == null)
            signKey = key + ".sign";

        return carousel.service(signKey, null, null, 2, JSONObject.class);
    }

    @Override
    public String id() {
        return sign().getString("id");
    }

    @Override
    protected String getKey() {
        return key;
    }
}
