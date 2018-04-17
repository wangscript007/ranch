package org.lpw.ranch.user.helper;

import com.alibaba.fastjson.JSONArray;
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
    private String signInKey;
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
        if (validator.isEmpty(uid))
            return new JSONObject();

        if (uidKey == null)
            uidKey = key + ".find-by-uid";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("uid", uid);

        return carousel.service(uidKey, null, parameter, true, JSONObject.class);
    }

    @Override
    public String findIdByUid(String uid, String defaultValue) {
        if (validator.isEmpty(uid))
            return defaultValue;

        String id = findByUid(uid).getString("id");

        return validator.isEmpty(id) ? defaultValue : id;
    }

    @Override
    public JSONObject findOrSign(String idUidCode) {
        if (validator.isEmpty(idUidCode))
            return new JSONObject();

        if (uidKey == null)
            uidKey = key + ".find-sign";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("idUidCode", idUidCode);

        return carousel.service(uidKey, null, parameter, true, JSONObject.class);
    }

    @Override
    public String[] uids(String id) {
        JSONObject object = get(id);
        if (validator.isEmpty(object))
            return new String[0];

        JSONArray auth = object.getJSONArray("auth");
        if (validator.isEmpty(auth))
            return new String[0];

        String[] uids = new String[auth.size()];
        for (int i = 0; i < uids.length; i++)
            uids[i] = auth.getJSONObject(i).getString("uid");

        return uids;
    }

    @Override
    public boolean exists(String id) {
        return get(id).size() > 1;
    }

    @Override
    public JSONObject signIn(String uid, String password, int type) {
        if (signInKey == null)
            signInKey = key + ".sign-in";

        Map<String, String> parameter = new HashMap<>();
        parameter.put("uid", uid);
        parameter.put("password", password);
        parameter.put("type", "" + type);

        return carousel.service(signInKey, null, parameter, true, JSONObject.class);
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
