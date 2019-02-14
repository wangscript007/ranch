package org.lpw.ranch.editor.label;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(LabelModel.NAME + ".dao")
class LabelDaoImpl implements LabelDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<LabelModel> query(String name) {
        return liteOrm.query(new LiteQuery(LabelModel.class).where("c_name=?"), new Object[]{name});
    }

    @Override
    public void save(LabelModel label) {
        liteOrm.save(label);
    }

    @Override
    public void delete(String editor) {
        liteOrm.delete(new LiteQuery(LabelModel.class).where("c_editor=?"), new Object[]{editor});
    }
}
