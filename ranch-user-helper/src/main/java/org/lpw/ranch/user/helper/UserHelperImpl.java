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
    private String codeKey;
    private String uidKey;
    private String signKey;

    @Override
    public JSONObject findByCode(String code) {
        if (codeKey == null)
            codeKey = key + ".find-by-code";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("code", code);

        return carousel.service(codeKey, null, parameter, true, JSONObject.class);
    }

    @Override
    public JSONObject findByUid(String uid) {
        if (uidKey == null)
            uidKey = key + ".find-by-uid";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("uid", uid);

        return carousel.service(uidKey, null, parameter, true, JSONObject.class);
    }

    @Override
    public String findIdByUid(String uid) {
        return findByUid(uid).getString("id");
    }

    @Override
    public boolean exists(String id) {
        return get(id).size() > 1;
    }

    @Override
    public boolean signIn() {
        return !sign().isEmpty();
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
