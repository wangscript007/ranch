package org.lpw.ranch.doc.topic;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(TopicModel.NAME + ".dao")
class TopicDaoImpl implements TopicDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<TopicModel> query(String classify, String subject, String label, Audit audit, Recycle recycle, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder().append(recycle.getSql());
        if (audit != null)
            where.append(" and ").append(audit.getSql());
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_classify", DaoOperation.Equals, classify, true);
        daoHelper.like(null, where, args, "c_subject", subject, true);
        daoHelper.like(null, where, args, "c_label", label, true);

        return liteOrm.query(new LiteQuery(TopicModel.class).where(where.toString()).order("c_sort,c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<TopicModel> query(String doc) {
        return liteOrm.query(new LiteQuery(TopicModel.class).where("doc=?"), new Object[]{doc});
    }

    @Override
    public void save(TopicModel topic) {
        liteOrm.save(topic);
    }

    @Override
    public void audit(String doc, Audit audit) {
        liteOrm.update(new LiteQuery(TopicModel.class).set(audit.getSql()).where("c_doc=?"), new Object[]{doc});
    }

    @Override
    public void recycle(String doc, Recycle recycle) {
        liteOrm.update(new LiteQuery(TopicModel.class).set(recycle.getSql()).where("c_doc=?"), new Object[]{doc});
    }

    @Override
    public void delete(String doc) {
        liteOrm.delete(new LiteQuery(TopicModel.class).where("c_doc=?"), new Object[]{doc});
    }
}
