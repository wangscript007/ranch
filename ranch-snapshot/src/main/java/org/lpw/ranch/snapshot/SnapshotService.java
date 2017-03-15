package org.lpw.ranch.snapshot;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface SnapshotService {
    /**
     * 获取快照信息集。
     *
     * @param ids ID集。
     * @return 快照信息集。
     */
    JSONObject get(String[] ids);

    /**
     * 创建新快照。
     *
     * @param data    数据。
     * @param content 内容。
     * @return 快照ID。
     */
    String create(String data, String content);
}
