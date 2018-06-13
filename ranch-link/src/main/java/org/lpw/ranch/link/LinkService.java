package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface LinkService {
    /**
     * 检索关联数据集。
     *
     * @param type 类型。
     * @param id1  ID1，如果为空则检索ID2，如果不为空则检索ID1。
     * @param id2  ID2。
     * @return 关联数据集。
     */
    JSONObject query(String type, String id1, String id2);

    /**
     * 统计关联数据量。
     *
     * @param type 类型。
     * @param id1  ID1，如果为空则统计ID2，不为空则统计ID1。
     * @param id2  ID2。
     * @return 关联数据量。
     */
    int count(String type, String id1, String id2);

    /**
     * 查找关联数据。
     *
     * @param type 类型。
     * @param id1  ID1。
     * @param id2  ID2。
     * @return 关联数据，不存在则返回空JSON。
     */
    JSONObject find(String type, String id1, String id2);

    /**
     * 保存关联数据。
     *
     * @param link 关联数据。
     * @return 关联数据。
     */
    JSONObject save(LinkModel link);

    /**
     * 删除关联数据。
     *
     * @param type 类型。
     * @param id1  ID1。
     * @param id2  ID2。
     */
    void delete(String type, String id1, String id2);
}
