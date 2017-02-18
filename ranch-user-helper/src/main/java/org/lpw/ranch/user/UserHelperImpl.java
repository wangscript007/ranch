package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.ServiceHelperSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service("ranch.user.helper")
public class UserHelperImpl extends ServiceHelperSupport implements UserHelper {
    @Value("${ranch.user.key:ranch.user}")
    private String key;
    private String signKey;

    @Override
    public JSONObject sign() {
        if (signKey == null)
            signKey = key + ".sign";

        return carousel.service(signKey, null, null, 2, JSONObject.class);
    }

    @Override
    protected String getKey() {
        return key;
    }
}
