package org.lpw.ranch.doc.relation;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface RelationService {
    /**
     * 获取关联文档集。
     *
     * @param doc 文档。
     * @return 关联文档集。
     */
    JSONObject find(String doc);

    /**
     * 保存关联。
     *
     * @param doc    文档。
     * @param relate 关联文档。
     * @param type   类型。
     * @param sort   顺序。
     */
    void save(String doc, String relate, String type, int sort);

    /**
     * 清空关联。
     */
    void clear();
}
