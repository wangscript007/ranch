package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
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
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".service")
public class ConsoleServiceImpl implements ConsoleService {
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
    @Value("${" + ConsoleModel.NAME + ".root:/WEB-INF/ui/}")
    private String root;
    private Map<String, JSONObject> map = new ConcurrentHashMap<>();

    @Override
    public boolean signUp(String domain) {
        return json.hasTrue(menu(domain), "sign-up");
    }

    @Override
    public boolean permit(String domain) {
        return permit(menu(domain));
    }

    private boolean permit(JSONObject menu) {
        return !menu.containsKey("grade") || menu.getIntValue("grade") <= userHelper.grade();
    }

    @Override
    public JSONArray menus(String domain) {
        JSONObject menu = menu(domain);

        return permit(menu) ? menu.getJSONArray("menus") : new JSONArray();
    }

    private JSONObject menu(String domain) {
        return map.computeIfAbsent(domain, key -> {
            JSONObject object = json.toObject(io.readAsString(context.getAbsolutePath(root + domain + "/menu.json")));
            if (object == null) {
                logger.warn(null, "读取[{}]菜单配置失败！", key);

                return new JSONObject();
            }

            if (logger.isInfoEnable())
                logger.info("载入菜单配置[{}:{}]。", key, object);

            return object;
        });
    }

    @Override
    public JSONObject meta(String domain, String key) {
        JSONObject meta = metaHelper.get(domain, key);
        if (meta == null)
            return new JSONObject();

        String prefix = meta.getString("key");
        setLabel(prefix, meta, "props", "name");
        for (String k : meta.keySet()) {
            if (k.equals("key") || k.equals("uri") || k.equals("props"))
                continue;

            JSONObject object = meta.getJSONObject(k);
            if (object.containsKey("search"))
                setLabel(prefix, object, "search", "name");
            if (object.containsKey("ops"))
                setLabel(ConsoleModel.NAME + ".op", object, "ops", "type");
            if (object.containsKey("toolbar"))
                setLabel(ConsoleModel.NAME + ".op", object, "toolbar", "type");
        }

        return meta;
    }

    private void setLabel(String prefix, JSONObject object, String key, String k) {
        if (!object.containsKey(key))
            return;

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
}
