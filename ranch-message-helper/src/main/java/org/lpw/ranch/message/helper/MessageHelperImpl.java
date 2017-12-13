package org.lpw.ranch.message.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Numeric;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.message.helper")
public class MessageHelperImpl implements MessageHelper {
    @Inject
    private Numeric numeric;
    @Inject
    private Sign sign;
    @Inject
    private Generator generator;
    @Inject
    private Carousel carousel;
    @Value("${ranch.message.key:ranch.message}")
    private String key;
    private String sendKey;
    private String noticeKey;

    @Override
    public String send(Type type, String receiver, Format format, String content, int deadline) {
        if (sendKey == null)
            sendKey = key + ".send";

        return send(sendKey, newParameter(type, receiver, format, content, deadline));
    }

    @Override
    public String notice(Type type, String receiver, String content, int deadline) {
        if (noticeKey == null)
            noticeKey = key + ".notice";

        Map<String, String> parameter = newParameter(type, receiver, null, content, deadline);
        sign.put(parameter, null);

        return send(noticeKey, parameter);
    }

    private Map<String, String> newParameter(Type type, String receiver, Format format, String content, int deadline) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("type", numeric.toString(type.ordinal(), "0"));
        parameter.put("receiver", receiver);
        if (format != null)
            parameter.put("format", numeric.toString(format.ordinal(), "0"));
        parameter.put("content", content);
        parameter.put("deadline", numeric.toString(deadline, "0"));
        parameter.put("code", generator.random(32));

        return parameter;
    }

    private String send(String key, Map<String, String> parameter) {
        JSONObject object = carousel.service(key, null, parameter, false);

        return object.getString(object.getIntValue("code") == 0 ? "data" : "message");
    }
}
