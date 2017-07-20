package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.tephra.util.Json;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".service")
public class ConsoleServiceImpl implements ConsoleService {
    @Inject
    private Json json;
    @Inject
    private ClassifyHelper classifyHelper;

    @Override
    public JSONArray menus() {
        return new JSONArray();
    }

    @Override
    public JSONObject meta(String service) {
        JSONObject object = json.toObject(classifyHelper.value(ConsoleModel.NAME + ".meta", service));

        return object == null ? new JSONObject() : object;
    }
}
