package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
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
    private Logger logger;
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
            if (!menu.containsKey("menus"))
                return new JSONArray();

            JSONArray menus = json.toArray(menu.getJSONArray("menus").toJSONString());

            return menus;
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

            JSONObject meta = metaHelper.get(domain, service.substring(0, index));
            String name = service.substring(index);
            if (!meta.containsKey(name))
                continue;

            JSONObject m = meta.getJSONObject(name);
            JSONArray items = new JSONArray();
            operation(m, "toolbar", items);
            operation(m, "ops", items);
            if (!items.isEmpty())
                menu.put("items", items);
        }
    }

    private void operation(JSONObject meta, String name, JSONArray items) {
        if (meta.containsKey(name))
            items.addAll(meta.getJSONArray(name));
    }
}
