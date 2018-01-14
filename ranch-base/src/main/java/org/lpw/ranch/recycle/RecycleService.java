package org.lpw.ranch.recycle;

import com.alibaba.fastjson.JSONObject;

/**
 * 回收站服务。
 *
 * @author lpw
 */
public interface RecycleService {
    /**
     * 删除分类信息。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 检索回收站分类信息集。
     *
     * @return 分类信息集。
     */
    JSONObject recycle();

    /**
     * （从回收站）还原分类数据。
     *
     * @param id ID值。
     */
    void restore(String id);
}
