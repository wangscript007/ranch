package org.lpw.ranch.notice.helper;

import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Component("ranch.notice.helper")
public class NoticeHelperImpl implements NoticeHelper {
    @Inject
    private Converter converter;
    @Inject
    private Carousel carousel;
    @Value("${ranch.notice.key:ranch.notice}")
    private String key;
    private String sendKey;
    private String sendsKey;

    @Override
    public void send(String user, String type, String subject, String content) {
        if (sendKey == null)
            sendKey = key + ".send";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("user", user);
        parameter.put("type", type);
        parameter.put("subject", subject);
        parameter.put("content", content);
        carousel.service(sendKey, null, parameter, false);
    }

    @Override
    public void send(String[] users, String type, String subject, String content) {
        if (sendsKey == null)
            sendsKey = key + ".sends";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("users", converter.toString(users, ","));
        parameter.put("type", type);
        parameter.put("subject", subject);
        parameter.put("content", content);
        carousel.service(sendsKey, null, parameter, false);
    }
}
