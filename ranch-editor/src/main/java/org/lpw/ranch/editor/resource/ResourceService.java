package org.lpw.ranch.editor.resource;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ResourceService {
    /**
     * 检索资源集。
     *
     * @param type  类型，为空则不限制。
     * @param name  名称，为空则不限制。
     * @param state 状态，为空则不限制。
     * @param uid   用户，为空则不限制。
     * @return 资源集。
     */
    JSONObject query(String type, String name, int state, String uid);

    /**
     * 获取上架资源集。
     *
     * @param type 类型。
     * @return 资源集。
     */
    JSONObject onsale(String type);

    /**
     * 保存资源信息。
     *
     * @param resource 资源信息。
     * @return 资源信息。
     */
    JSONObject save(ResourceModel resource);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);
}
