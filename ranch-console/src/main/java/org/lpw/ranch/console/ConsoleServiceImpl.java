package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.ranch.meta.MetaService;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Json;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".service")
public class ConsoleServiceImpl implements ConsoleService {
    @Inject
    private Json json;
    @Inject
    private Carousel carousel;
    @Inject
    private MetaService metaService;
    @Inject
    private ClassifyHelper classifyHelper;

    @Override
    public JSONArray menus() {
        return json.toArray(classifyHelper.value(ConsoleModel.NAME, "menu"));
    }

    @Override
    public JSONObject meta(String service) {
        JSONObject object = json.toObject(classifyHelper.value(ConsoleModel.NAME + ".meta", service));
        if (object == null)
            object = metaService.get(service);

        return object == null ? new JSONObject() : object;
    }

    @Override
    public JSONObject service(String service, Map<String, String> parameter) {
        return carousel.service(service, null, parameter, false);
    }
}
