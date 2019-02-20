package org.lpw.ranch.editor;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lpw
 */
@Repository(EditorModel.NAME + ".dao")
class EditorDaoImpl implements EditorDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<EditorModel> query(Set<String> ids, int template, String type, String name, String label, int modified, Set<Integer> states,
                                       Timestamp createStart, Timestamp createEnd, Timestamp modifyStart, Timestamp modifyEnd,
                                       Order order, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.in(where, args, "c_id", ids);
        daoHelper.where(where, args, "c_template", DaoOperation.Equals, template);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_modified", DaoOperation.GreaterEquals, modified);
        daoHelper.in(where, args, "c_state", states);
        daoHelper.where(where, args, "c_create", DaoOperation.GreaterEquals, createStart);
        daoHelper.where(where, args, "c_create", DaoOperation.LessEquals, createEnd);
        daoHelper.where(where, args, "c_modify", DaoOperation.GreaterEquals, modifyStart);
        daoHelper.where(where, args, "c_modify", DaoOperation.LessEquals, modifyEnd);
        daoHelper.like(null, where, args, "c_name", name);
        daoHelper.like(null, where, args, "c_label", label);

        return liteOrm.query(new LiteQuery(EditorModel.class).where(where.toString()).order(order.by())
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<EditorModel> query(int template, String type, int state, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(EditorModel.class).where("c_template=? and c_type=? and c_state=?")
                .size(pageSize).page(pageNum), new Object[]{template, type, state});
    }

    @Override
    public PageList<EditorModel> query(Timestamp[] modify) {
        return liteOrm.query(new LiteQuery(EditorModel.class).where("c_modify between ? and ?"), new Object[]{modify[0], modify[1]});
    }

    @Override
    public EditorModel findById(String id) {
        return liteOrm.findById(EditorModel.class, id);
    }

    @Override
    public void save(EditorModel editor) {
        liteOrm.save(editor);
        liteOrm.close();
    }

    @Override
    public void sort(String id, String type, int sort) {
        liteOrm.update(new LiteQuery(EditorModel.class).set("c_sort=?").where("c_id=? and c_template>0 and c_type=?"), new Object[]{sort, id});
    }
}
