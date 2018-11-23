package org.lpw.ranch.push.helper;

import com.alibaba.fastjson.JSONObject;
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
@Service("ranch.push.helper")
public class PushHelprImpl implements PushHelper {
    @Inject
    private Validator validator;
    @Inject
    private Sign sign;
    @Inject
    private Carousel carousel;
    @Value("${ranch.push.key:ranch.push}")
    private String key;

    @Override
    public boolean send(String key, String user, String receiver, JSONObject args) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("user", user);
        parameter.put("receiver", receiver);
        if (!validator.isEmpty(args))
            parameter.put("args", args.toJSONString());
        sign.put(parameter, null);
        JSONObject object = carousel.service(this.key + ".send", null, parameter, false);

        return object.containsKey("data") && object.getBoolean("data");
    }

    @Override
    public boolean send(String user, String appCode, String subject, String content, JSONObject args) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("user", user);
        parameter.put("appCode", appCode);
        parameter.put("subject", subject);
        parameter.put("content", content);
        if (!validator.isEmpty(args))
            parameter.put("args", args.toJSONString());
        sign.put(parameter, null);
        JSONObject object = carousel.service(key + ".send-user", null, parameter, false);

        return object.containsKey("data") && object.getBoolean("data");
    }
}
