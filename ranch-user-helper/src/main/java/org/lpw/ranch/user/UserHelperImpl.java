package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.helper")
public class UserHelperImpl implements UserHelper {
    @Inject
    private Validator validator;
    @Inject
    private Carousel carousel;
    @Value("${ranch.user.key:ranch.user}")
    private String key;
    private String getKey;
    private String signKey;

    @Override
    public JSONObject get(String id) {
        if (getKey == null)
            getKey = key + ".get";

        return carousel.get(getKey, id);
    }

    @Override
    public JSONArray fill(JSONArray array, String[] names) {
        if (validator.isEmpty(array) || validator.isEmpty(names))
            return array;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            for (String name : names)
                object.put(name, get(object.getString(name)));
        }

        return array;
    }

    @Override
    public JSONObject sign() {
        if (signKey == null)
            signKey = key + ".sign";

        return carousel.service(signKey, null, null, 2);
    }
}
