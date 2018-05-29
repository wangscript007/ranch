package org.lpw.ranch.editor.graphic;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface GraphicService {
    /**
     * 检索图形集。
     *
     * @param type 类型，为空则表示全部。
     * @param name 名称，为空则表示全部。
     * @return 图形集。
     */
    JSONObject query(String type, String name);

    /**
     * 保存图形信息。
     *
     * @param graphic 图形信息。
     * @return 图形信息。
     */
    JSONObject save(GraphicModel graphic);

    /**
     * 删除图形。
     *
     * @param id ID值。
     */
    void delete(String id);
}
