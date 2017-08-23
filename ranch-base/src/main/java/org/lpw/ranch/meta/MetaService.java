package org.lpw.ranch.meta;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MetaService {
    /**
     * 获取元数据。
     *
     * @param key 模块KEY。
     * @return 元数据；如果不存在则返回null。
     */
    JSONObject get(String key);
}
