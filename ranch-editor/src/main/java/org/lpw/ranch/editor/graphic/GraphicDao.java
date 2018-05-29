package org.lpw.ranch.editor.graphic;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface GraphicDao {
    PageList<GraphicModel> query(String type, String name, int pageSize, int pageNum);

    GraphicModel findById(String id);

    void save(GraphicModel graphic);

    void delete(String id);
}
