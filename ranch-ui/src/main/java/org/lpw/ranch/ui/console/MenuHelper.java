package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface MenuHelper {
    /**
     * 获取菜单。
     *
     * @param domain 所属域。
     * @param all    全部，不过滤。
     * @return 菜单。
     */
    JSONArray get(String domain, boolean all);
}
