package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.crosier.CrosierService;
import org.lpw.ranch.user.crosier.CrosierValid;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service(ConsoleModel.NAME + ".menu.helper")
public class MenuHelperImpl implements MenuHelper, CrosierValid {
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
    private Numeric numeric;
    @Inject
    private Logger logger;
    @Inject
    private CrosierService crosierService;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MetaHelper metaHelper;
    @Value("${" + ConsoleModel.NAME + ".console:/WEB-INF/ui/console/}")
    private String console;
    private Map<String, JSONArray> map = new ConcurrentHashMap<>();

    @Override
    public JSONArray get(boolean all) {
        if (all) {
            return map.computeIfAbsent("all", key -> {
                JSONArray menus = get();
                operation(menus);

                return menus;
            });
        }

        return map.computeIfAbsent(numeric.toString(userHelper.grade(), "0"), key -> permit(get()));
    }

    private JSONArray get() {
        return json.toArray(map.get("").toJSONString());
    }

    private void operation(JSONArray menus) {
        if (validator.isEmpty(menus))
            return;

        for (int i = 0, size = menus.size(); i < size; i++) {
            JSONObject menu = menus.getJSONObject(i);
            if (menu.containsKey("items")) {
                operation(menu.getJSONArray("items"));

                continue;
            }

            String service = menu.getString("service").replace('.', '/');
            int index = service.lastIndexOf('/') + 1;
            if (index == 0)
                continue;

            JSONArray items = new JSONArray();
            operation(metaHelper.get(service.substring(0, index)), service.substring(index), new String[]{"toolbar", "ops"}, items, 0);
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
            Map<String, String> parameter = object.containsKey("parameter") ? json.toMap(object.getJSONObject("parameter")) : new HashMap<>();
            if (object.containsKey("service") && !crosierService.permit(object.getString("service"), parameter))
                continue;

            if (object.containsKey("items")) {
                JSONArray items = permit(object.getJSONArray("items"));
                if (!items.isEmpty()) {
                    object.put("items", items);
                    array.add(object);
                }
            } else
                array.add(object);
        }

        return array;
    }

    @Override
    public void crosierValid(int grade) {
        map.computeIfAbsent("", key -> load());
        map.remove(numeric.toString(grade, "0"));
    }

    private JSONArray load() {
        JSONArray array = json.toArray(io.readAsString(context.getAbsolutePath(console + "menu.json")));
        if (array == null) {
            logger.warn(null, "读取菜单配置失败！");

            return new JSONArray();
        }

        if (logger.isInfoEnable())
            logger.info("载入菜单配置[{}]。", array);

        return array;
    }
}
