package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
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
    private Logger logger;
    @Inject
    private UserHelper userHelper;
    @Value("${" + ConsoleModel.NAME + ".root:/WEB-INF/ui/}")
    private String root;
    private Map<String, JSONObject> map = new ConcurrentHashMap<>();

    @Override
    public JSONArray get(String domain, boolean all) {
        return cache.computeIfAbsent(ConsoleModel.NAME + ".menu:" + domain + ":" + all + ":" + userHelper.grade(), key -> {
            JSONObject menu = menu(domain);
            if (!menu.containsKey("menus"))
                return new JSONArray();

            JSONArray menus = menu.getJSONArray("menus");
            if (all || menus.isEmpty())
                return menus;

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
}
