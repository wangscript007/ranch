package org.lpw.ranch.doc.relation;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(RelationModel.NAME + ".dao")
class RelationDaoImpl implements RelationDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<RelationModel> query(String doc) {
        return liteOrm.query(new LiteQuery(RelationModel.class).where("c_doc=?").order("c_sort"), new Object[]{doc});
    }

    @Override
    public void save(RelationModel relation) {
        liteOrm.save(relation);
    }

    @Override
    public void clear() {
        liteOrm.delete(new LiteQuery(RelationModel.class), null);
    }
}
