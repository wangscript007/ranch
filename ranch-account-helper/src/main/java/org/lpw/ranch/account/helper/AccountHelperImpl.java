package org.lpw.ranch.account.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.account.helper")
public class AccountHelperImpl implements AccountHelper {
    @Inject
    private Converter converter;
    @Inject
    private Carousel carousel;
    @Value("${ranch.account.key:ranch.account}")
    private String key;

    @Override
    public JSONObject deposit(String user, String owner, int type, int amount, Map<String, String> map) {
        map.put("user", user);
        map.put("owner", owner);
        map.put("type", converter.toString(type, "0"));
        map.put("amount", converter.toString(amount, "0"));

        return carousel.service(key + ".deposit", null, map, false, JSONObject.class);
    }

    @Override
    public boolean pass(String logId) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("ids", logId);
        JSONObject object = carousel.service(key + ".log.pass", null, parameter, false);

        return object.getIntValue("code") == 0;
    }
}
