package org.lpw.ranch.gps;

import net.sf.json.JSONObject;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(GpsModel.NAME + ".service")
public class GpsServiceImpl implements GpsService {
    private static final String[] ADDRESS = {"http://apis.map.qq.com/ws/geocoder/v1/?location=", "&get_poi=0&key="};

    @Autowired
    protected Validator validator;
    @Autowired
    protected Http http;
    @Value("${ranch.gps.qqlbs.key:}")
    protected String qqlbsKey;

    @Override
    public JSONObject address(String lat, String lng) {
        JSONObject object = new JSONObject();
        if (validator.isEmpty(qqlbsKey))
            return object;

        String string = http.get(new StringBuilder(ADDRESS[0]).append(lat).append(',').append(lng)
                .append(ADDRESS[1]).append(qqlbsKey).toString(), null, "");
        if (validator.isEmpty(string))
            return object;

        JSONObject json = JSONObject.fromObject(string);
        if (json.getInt("status") != 0)
            return object;

        JSONObject result = json.getJSONObject("result");
        object.put("address", result.getString("address"));
        JSONObject component = result.getJSONObject("address_component");
        object.put("component", component);

        return object;
    }
}
