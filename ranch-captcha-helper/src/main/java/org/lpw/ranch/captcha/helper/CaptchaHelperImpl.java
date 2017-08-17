package org.lpw.ranch.captcha.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.captcha.helper")
public class CaptchaHelperImpl implements CaptchaHelper {
    @Inject
    private Carousel carousel;
    @Value("${ranch.captcha.key:ranch.captcha}")
    private String captchaKey;

    @Override
    public boolean validate(String key, String code) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("code", code);
        JSONObject json = carousel.service(captchaKey + ".validate", null, parameter, false);

        return json.getIntValue("code") == 0 && json.getBooleanValue("data");
    }
}
