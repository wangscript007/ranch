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
        return json.toArray(" [{\n" +
                "            \"name\": \"主菜单\",\n" +
                "            \"items\": [{\n" +
                "                \"name\": \"子菜单一\",\n" +
                "                \"service\": \"service key\"\n" +
                "            }, {\n" +
                "                \"name\": \"子菜单二\",\n" +
                "                \"service\": \"ranch.account.query\"\n" +
                "            }]\n" +
                "        }, {\n" +
                "            \"name\": \"系统设置\",\n" +
                "            \"items\": [{\n" +
                "                \"name\": \"用户管理\",\n" +
                "                \"service\": \"ranch.user.query\"\n" +
                "            }, {\n" +
                "                \"name\": \"账户管理\",\n" +
                "                \"service\": \"ranch.account.query\"\n" +
                "            }]\n" +
                "        }]");
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
