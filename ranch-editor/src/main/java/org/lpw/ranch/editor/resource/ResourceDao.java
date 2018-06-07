package org.lpw.ranch.editor.resource;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface ResourceDao {
    PageList<ResourceModel> query(String type, String name, int state, String user, int pageSize, int pageNum);

    ResourceModel findById(String id);

    void save(ResourceModel resource);

    void delete(String id);
}
