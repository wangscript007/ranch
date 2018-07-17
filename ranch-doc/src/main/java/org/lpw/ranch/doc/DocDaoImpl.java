package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(DocModel.NAME + ".dao")
class DocDaoImpl implements DocDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
    }

    @Override
    public PageList<DocModel> query(String key, String author, String subject, String label,
                                    Audit audit, Recycle recycle, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder().append(recycle.getSql());
        if (audit != null)
            where.append(" and ").append(audit.getSql());
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_key", DaoOperation.Equals, key, true);
        daoHelper.where(where, args, "c_author", DaoOperation.Equals, author, true);
        daoHelper.like(null, where, args, "c_subject", subject, true);
        daoHelper.like(null, where, args, "c_label", label, true);

        return liteOrm.query(new LiteQuery(DocModel.class).where(where.toString()).order("c_sort,c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public void save(DocModel doc) {
        liteOrm.save(doc);
    }
}
