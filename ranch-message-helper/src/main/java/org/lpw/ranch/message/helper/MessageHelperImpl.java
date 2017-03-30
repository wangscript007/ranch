package org.lpw.ranch.message.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.util.Generator;
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
    private Sign sign;
    @Inject
    private Generator generator;
    @Inject
    private Carousel carousel;
    @Value("${ranch.message.key:ranch.message}")
    private String key;
    private String sendKey;
    private String notifyKey;

    @Override
    public String send(Type type, String receiver, Format format, String content) {
        if (sendKey == null)
            sendKey = key + ".send";

        return send(sendKey, newParameter(type, receiver, format, content));
    }

    @Override
    public String notify(Type type, String receiver, String content) {
        if (notifyKey == null)
            notifyKey = key + ".notify";

        Map<String, String> parameter = newParameter(type, receiver, Format.Notify, content);
        sign.put(parameter, null);

        return send(notifyKey, parameter);
    }

    private Map<String, String> newParameter(Type type, String receiver, Format format, String content) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("type", "" + type.ordinal());
        parameter.put("receiver", receiver);
        parameter.put("format", "" + format.ordinal());
        parameter.put("content", content);
        parameter.put("code", generator.random(32));

        return parameter;
    }

    private String send(String key, Map<String, String> parameter) {
        JSONObject object = carousel.service(key, null, parameter, false);

        return object.getString(object.getIntValue("code") == 0 ? "data" : "message");
    }
}
