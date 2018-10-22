package org.lpw.ranch.link.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface LinkHelper {
    /**
     * 检索关联数据集。
     *
     * @param type     类型。
     * @param id1      ID1，如果为空则检索ID2，如果不为空则检索ID1。
     * @param id2      ID2。
     * @param pageSize 每页显示记录数。
     * @param pageNum  当前显示页数。
     * @return 关联数据集。
     */
    JSONObject query(String type, String id1, String id2, int pageSize, int pageNum);

    /**
     * 保存关联数据。
     *
     * @param type 类型。
     * @param id1  ID1。
     * @param id2  ID2。
     * @return 关联数据。
     */
    JSONObject save(String type, String id1, String id2);

    /**
     * 删除关联数据。
     *
     * @param type 类型。
     * @param id1  ID1。
     * @param id2  ID2。
     */
    void delete(String type, String id1, String id2);
}
