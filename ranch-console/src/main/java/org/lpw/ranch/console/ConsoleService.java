package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface ConsoleService {
    /**
     * 是否允许访问验证器Bean名称。
     */
    String VALIDATOR_PERMIT = ConsoleModel.NAME + ".valiator.permit";

    /**
     * 验证用户权限。
     *
     * @return 如果允许则返回true；否则返回fale。
     */
    boolean permit();

    /**
     * 获取菜单集。
     *
     * @return 菜单集。
     */
    JSONArray menus();

    /**
     * 获取模块元数据。
     *
     * @param key 模块key。
     * @return 模块元数据；如果不存在则返回空JSON。
     */
    JSONObject meta(String key);

    /**
     * 执行服务。
     *
     * @param service   服务key。
     * @param parameter 参数集。
     * @return 服务结果。
     */
    JSONObject service(String service, Map<String, String> parameter);
}
