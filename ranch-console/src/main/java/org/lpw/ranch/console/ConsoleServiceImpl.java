package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.MetaHelper;
import org.lpw.tephra.storage.StorageListener;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
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
    private Logger logger;
    @Inject
    private Carousel carousel;
    @Inject
    private MetaHelper metaHelper;
    @Inject
    private UserHelper userHelper;
    @Value("${" + ConsoleModel.NAME + ".menu:/WEB-INF/menu.json}")
    private String menu;
    private JSONArray menus;

    @Override
    public boolean permit() {
        return userHelper.sign().getIntValue("grade") >= 90;
    }

    @Override
    public JSONArray menus() {
        return permit() ? menus : new JSONArray();
    }

    @Override
    public JSONObject meta(String key) {
        JSONObject meta = metaHelper.get(key);
        if (meta == null)
            return new JSONObject();

        meta = json.toObject(meta.toJSONString());
        String prefix = meta.getString("key");
        setLabel(prefix, meta, "props", "name");
        for (String k : meta.keySet()) {
            if (k.equals("key") || k.equals("props"))
                continue;

            JSONObject object = meta.getJSONObject(k);
            if (object.containsKey("search"))
                setLabel(prefix, object, "search", "name");
            if (object.containsKey("ops"))
                setLabel("ranch.console.op", object, "ops", "type");
            if (object.containsKey("toolbar"))
                setLabel("ranch.console.op", object, "toolbar", "type");
        }

        return meta;
    }

    private void setLabel(String prefix, JSONObject object, String key, String k) {
        JSONArray array = object.getJSONArray(key);
        for (int i = 0, size = array.size(); i < size; i++)
            setLabel(prefix, array.getJSONObject(i), k);
    }

    private void setLabel(String prefix, JSONObject object, String key) {
        String label = null;
        if (object.containsKey("label")) {
            label = object.getString("label");
            if (label.charAt(0) == '.')
                label = prefix + label;
        } else if (key != null && object.containsKey(key))
            label = prefix + "." + object.getString(key);
        if (label != null)
            object.put("label", message.get(label));

        if (object.containsKey("labels")) {
            String labels = object.getString("labels");
            if (labels.charAt(0) == '.')
                labels = prefix + labels;
            JSONArray array = new JSONArray();
            array.addAll(Arrays.asList(message.getAsArray(labels)));
            object.put("labels", array);
        } else if (object.containsKey("values")) {
            JSONObject values = object.getJSONObject("values");
            for (String k : values.keySet()) {
                String v = values.getString(k);
                if (v.charAt(0) == '.')
                    v = label + v;
                values.put(k, message.get(v));
            }
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
        if (logger.isInfoEnable())
            logger.info("载入菜单配置[{}:{}]。", absolutePath, menus.toJSONString());
    }
}
