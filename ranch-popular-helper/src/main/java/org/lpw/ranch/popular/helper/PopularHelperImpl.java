package org.lpw.ranch.popular.helper;

import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.popular.helper")
public class PopularHelperImpl implements PopularHelper {
    @Inject
    private Validator validator;
    @Inject
    private Sign sign;
    @Inject
    private Carousel carousel;
    @Value("${ranch.popular.key:ranch.popular}")
    private String key;

    @Override
    public void increase(String key, String value) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("value", value);
        sign.put(parameter, null);
        carousel.service(this.key + ".increase", null, parameter, false);
    }
}
