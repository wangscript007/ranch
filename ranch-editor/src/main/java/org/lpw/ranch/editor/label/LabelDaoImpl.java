package org.lpw.ranch.editor.label;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Set;

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
    public PageList<LabelModel> query(Set<String> editors) {
        StringBuilder where = new StringBuilder("c_editor in(");
        for (int i = 0, size = editors.size(); i < size; i++) {
            if (i > 0)
                where.append(',');
            where.append('?');
        }

        return liteOrm.query(new LiteQuery(LabelModel.class).where(where.append(')').toString()), editors.toArray());
    }

    @Override
    public void save(LabelModel label) {
        liteOrm.save(label);
    }

    @Override
    public void rename(String oldName, String newName) {
        liteOrm.update(new LiteQuery(LabelModel.class).set("c_name=?").where("c_name=?"), new Object[]{newName, oldName});
        liteOrm.close();
    }

    @Override
    public void delete(String editor) {
        liteOrm.delete(new LiteQuery(LabelModel.class).where("c_editor=?"), new Object[]{editor});
    }

    @Override
    public void close() {
        liteOrm.close();
    }
}
