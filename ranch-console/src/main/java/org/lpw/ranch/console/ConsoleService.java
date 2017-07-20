package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ConsoleService {
    /**
     * 获取菜单集。
     *
     * @return 菜单集。
     */
    JSONArray menus();

    /**
     * 获取页面元数据。
     *
     * @param service 服务key。
     * @return 页面元数据；如果不存在则返回空JSON。
     */
    JSONObject meta(String service);
}
