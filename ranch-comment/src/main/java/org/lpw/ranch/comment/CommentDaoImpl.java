package org.lpw.ranch.comment;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
