package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(CommentModel.NAME + ".dao")
class CommentDaoImpl implements CommentDao {
    @Autowired
    protected LiteOrm liteOrm;

    @Override
    public void save(CommentModel comment) {
        liteOrm.save(comment);
    }

    @Override
    public void audit(String[] ids, Audit audit) {
        StringBuilder where = new StringBuilder("c_id in(");
        List<Object> args = new ArrayList<>();
        for (String id : ids) {
            if (args.size() > 0)
                where.append(',');
            where.append('?');
            args.add(id);
        }

        liteOrm.update(new LiteQuery(CommentModel.class).set(audit.getSql()).where(where.append(')').toString()), args.toArray());
    }
}
