package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.ranch.meta.MetaService;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.storage.StorageListener;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".service")
public class ConsoleServiceImpl implements ConsoleService, StorageListener {
    @Inject
    private Validator validator;
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private Json json;
    @Inject
    private Message message;
    @Inject
    private Carousel carousel;
    @Inject
    private MetaService metaService;
    @Inject
    private ClassifyHelper classifyHelper;
    @Value("${" + ConsoleModel.NAME + ".menu:/WEB-INF/menu.json}")
    private String menu;
    private JSONArray menus;

    @Override
    public JSONArray menus() {
        String menu = classifyHelper.value(ConsoleModel.NAME, "menu");
        if (validator.isEmpty(menu))
            return menus;

        return json.toArray(menu);
    }

    @Override
    public JSONObject meta(String key) {
        JSONObject meta = json.toObject(classifyHelper.value(ConsoleModel.NAME + ".meta", key));
        if (meta == null)
            meta = metaService.get(key);
        setLabel(meta);

        return meta == null ? new JSONObject() : meta;
    }

    private void setLabel(JSONObject meta) {
        if (validator.isEmpty(meta) || !meta.containsKey("props"))
            return;

        JSONArray props = meta.getJSONArray("props");
        for (int i = 0, size = props.size(); i < size; i++) {
            JSONObject prop = props.getJSONObject(i);
            String label = prop.containsKey("label") ? prop.getString("label") : (meta.getString("key") + "." + prop.getString("name"));
            prop.put("label", message.get(label));
        }
    }

    @Override
    public JSONObject service(String service, Map<String, String> parameter) {
        return carousel.service(service, null, parameter, false);
    }

    @Override
    public String getStorageType() {
        return Storages.TYPE_DISK;
    }

    @Override
    public String[] getScanPathes() {
        return new String[]{menu};
    }

    @Override
    public void onStorageChanged(String path, String absolutePath) {
        menus = json.toArray(io.readAsString(absolutePath));
    }
}
