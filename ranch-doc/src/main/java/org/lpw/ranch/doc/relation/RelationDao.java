package org.lpw.ranch.doc.relation;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface RelationDao {
    PageList<RelationModel> query(String doc);

    void save(RelationModel relation);

    void clear();
}
