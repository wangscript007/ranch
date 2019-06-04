package org.lpw.ranch.weixin.template.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.weixin.template.helper")
public class TemplateHelperImpl implements TemplateHelper {
    @Inject
    private Validator validator;
    @Inject
    private Carousel carousel;
    @Value("${ranch.weixin.key:ranch.weixin}")
    private String key;

    @Override
    public JSONObject send(String key, String receiver, String formId, JSONObject data, JSONObject args) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("receiver", receiver);
        if (!validator.isEmpty(formId))
            parameter.put("formId", formId);
        parameter.put("data", data.toJSONString());
        parameter.put("args", data.toJSONString());

        return carousel.service(this.key + ".template.send", null, parameter, false, JSONObject.class);
    }

    @Override
    public void putValue(JSONObject data, String key, String value) {
        JSONObject object = new JSONObject();
        object.put("value", value);
        data.put(key, object);
    }
}
