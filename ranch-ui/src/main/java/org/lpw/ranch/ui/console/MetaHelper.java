package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MetaHelper {
    /**
     * 获取元数据。
     *
     * @param key 模块KEY。
     * @param all 所有，不控制权限。
     * @return 元数据；如果不存在则返回null。
     */
    JSONObject get(String key, boolean all);
}
