package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.base.user")
public class UserImpl implements User {
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
    public JSONObject sign() {
        if (signKey == null)
            signKey = key + ".sign";

        return carousel.service(signKey, null, null, 2);
    }
}
