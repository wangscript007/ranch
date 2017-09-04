package org.lpw.ranch.account.helper;

import com.alibaba.fastjson.JSONArray;
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
    public JSONArray queryUser(String user, String owner, boolean fill) {
        return carousel.service(key + ".query-user", null, queryParameter(user, owner, fill), false, JSONArray.class);
    }

    @Override
    public JSONObject merge(String user, String owner, boolean fill) {
        return carousel.service(key + ".merge", null, queryParameter(user, owner, fill), false, JSONObject.class);
    }

    private Map<String, String> queryParameter(String user, String owner, boolean fill) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("user", user);
        if (owner != null)
            parameter.put("owner", owner);
        parameter.put("fill", "" + fill);

        return parameter;
    }

    @Override
    public JSONObject deposit(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map) {
        return change(".deposit", user, owner, type, channel, amount, pass, map);
    }

    @Override
    public JSONObject consume(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map) {
        return change(".consume", user, owner, type, channel, amount, pass, map);
    }

    @Override
    public JSONObject refund(String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map) {
        return change(".refund", user, owner, type, channel, amount, pass, map);
    }

    private JSONObject change(String key, String user, String owner, int type, String channel, int amount, boolean pass, Map<String, String> map) {
        if (map == null)
            map = new HashMap<>();
        map.put("user", user);
        map.put("owner", owner);
        map.put("type", converter.toString(type, "0"));
        map.put("channel", channel);
        map.put("amount", converter.toString(amount, "0"));
        JSONObject object = carousel.service(this.key + key, null, map, false, JSONObject.class);
        if (pass)
            pass(object.getString("logId"));

        return object;
    }

    @Override
    public boolean pass(String logIds) {
        return log("pass", logIds);
    }

    @Override
    public boolean reject(String logIds) {
        return log("reject", logIds);
    }

    private boolean log(String type, String ids) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("ids", ids);
        JSONObject object = carousel.service(key + ".log." + type, null, parameter, false);

        return object.getIntValue("code") == 0;
    }
}
