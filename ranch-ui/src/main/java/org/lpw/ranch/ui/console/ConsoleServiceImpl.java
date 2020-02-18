package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".service")
public class ConsoleServiceImpl implements ConsoleService {
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private Json json;
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
        return metaHelper.get(domain, key);
    }

    @Override
    public JSONObject service(String service, Map<String, String> parameter) {
        return carousel.service(service, null, parameter, false);
    }
}
