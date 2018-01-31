package org.lpw.ranch.device.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.util.Numeric;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.device.helper")
public class DeviceHelperImpl implements DeviceHelper {
    @Inject
    private Numeric numeric;
    @Inject
    private Sign sign;
    @Inject
    private Carousel carousel;
    @Value("${ranch.device.key:ranch.device}")
    private String key;

    @Override
    public JSONObject query(String user, String appCode, String type, String macId, String version, int pageSize, int pageNum) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("user", user);
        parameter.put("appCode", appCode);
        parameter.put("type", type);
        parameter.put("macId", macId);
        parameter.put("version", version);
        parameter.put("pageSize", numeric.toString(pageSize, "0"));
        parameter.put("pageNum", numeric.toString(pageNum, "0"));
        sign.put(parameter, null);

        return carousel.service(key + ".query", null, parameter, false, JSONObject.class);
    }

    @Override
    public JSONObject find(String appCode, String macId) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("appCode", appCode);
        parameter.put("macId", macId);
        sign.put(parameter, null);

        return carousel.service(key + ".find", null, parameter, true, JSONObject.class);
    }
}
