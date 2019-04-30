package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface LinkService {
    /**
     * 检索关联数据集。
     *
     * @param type 类型。
     * @param id1  ID1集，多个ID间以逗号分隔，如果为空则检索ID2，如果不为空则检索ID1。
     * @param id2  ID2集，多个ID间以逗号分隔。
     * @return 关联数据集。
     */
    JSONObject query(String type, String id1, String id2);

    /**
     * 统计关联数据数量。
     *
     * @param type 类型。
     * @param id1  ID1集，多个ID间以逗号分隔，如果为空则检索ID2，如果不为空则检索ID1。
     * @param id2  ID2集，多个ID间以逗号分隔。
     * @return 关联数据数量集。
     */
    JSONObject count(String type, String id1, String id2);

    /**
     * 判断关联数据是否存在。
     *
     * @param type 类型。
     * @param id1s ID1集。
     * @param id2s ID2集。
     * @return 关联数据是否存在集合。
     */
    JSONArray exists(String type, String[] id1s, String[] id2s);

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
