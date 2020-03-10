package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.crosier.CrosierService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(ConsoleModel.NAME + ".menu.helper")
public class MenuHelperImpl implements MenuHelper {
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private Json json;
    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private Logger logger;
    @Inject
    private CrosierService crosierService;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MetaHelper metaHelper;
    @Value("${" + ConsoleModel.NAME + ".root:/WEB-INF/ui/}")
    private String root;
    private Map<String, JSONObject> map = new ConcurrentHashMap<>();

    @Override
    public JSONArray get(String domain, boolean all) {
        if (all) {
            return cache.computeIfAbsent(ConsoleModel.NAME + ".menu:" + domain, key -> {
                JSONObject menu = menu(domain);
                if (!menu.containsKey("menus"))
                    return new JSONArray();

                JSONArray menus = json.toArray(menu.getJSONArray("menus").toJSONString());
                operation(domain, menus);

                return menus;
            }, false);
        }

        return cache.computeIfAbsent(ConsoleModel.NAME + ".menu:" + domain + ":" + userHelper.grade(), key -> {
            JSONObject menu = menu(domain);

            return menu.containsKey("menus") ? permit(json.toArray(menu.getJSONArray("menus").toJSONString())) : new JSONArray();
        }, false);
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

    private void operation(String domain, JSONArray menus) {
        if (validator.isEmpty(menus))
            return;

        for (int i = 0, size = menus.size(); i < size; i++) {
            JSONObject menu = menus.getJSONObject(i);
            if (menu.containsKey("items")) {
                operation(domain, menu.getJSONArray("items"));

                continue;
            }

            String service = menu.getString("service").replace('.', '/');
            int index = service.lastIndexOf('/') + 1;
            if (index == 0)
                continue;

            JSONArray items = new JSONArray();
            operation(metaHelper.get(domain, service.substring(0, index)), service.substring(index), new String[]{"toolbar", "ops"}, items, 0);
            if (!items.isEmpty())
                menu.put("items", items);
        }
    }

    private void operation(JSONObject meta, String name, String[] ops, JSONArray items, int depth) {
        if (meta == null || depth > 1 || validator.isEmpty(name) || !meta.containsKey(name))
            return;

        JSONObject m = meta.getJSONObject(name);
        for (String op : ops) {
            if (!m.containsKey(op))
                continue;

            JSONArray is = m.getJSONArray(op);
            items.addAll(is);
            for (int i = 0, size = is.size(); i < size; i++) {
                JSONObject obj = is.getJSONObject(i);
                operation(meta, obj.getString(obj.containsKey("service") ? "service" : "type"), ops, items, depth + 1);
            }
        }
    }

    private JSONArray permit(JSONArray menus) {
        JSONArray array = new JSONArray();
        for (int i = 0, size = menus.size(); i < size; i++) {
            JSONObject object = menus.getJSONObject(i);
            if (object.containsKey("items")) {
                JSONArray items = permit(object.getJSONArray("items"));
                if (!items.isEmpty()) {
                    object.put("items", items);
                    array.add(object);
                }

                continue;
            }

            Map<String, String> parameter = new HashMap<>();
            if (object.containsKey("parameter"))
                object.getJSONObject("parameter").forEach((key, value) -> parameter.put(key, converter.toString(value)));
            if (crosierService.permit(object.getString("service"), parameter))
                array.add(object);
        }

        return array;
    }
}
