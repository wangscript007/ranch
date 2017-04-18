package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.dao.jdbc.DataSource;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Repository(DocModel.NAME + ".dao")
class DocDaoImpl implements DocDao {
    @Inject
    private Validator validator;
    @Inject
    private DataSource dataSource;
    @Inject
    private LiteOrm liteOrm;
    private Map<String, String> counter = new ConcurrentHashMap<>();

    @Override
    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
    }

    @Override
    public PageList<DocModel> query(String key, String owner, String author, String subject, Audit audit, Recycle recycle, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder().append(recycle.getSql()).append(" and ").append(audit.getSql());
        List<Object> args = new ArrayList<>();
        append(where, args, "key", key);
        append(where, args, "owner", owner);
        append(where, args, "author", author);
        if (!validator.isEmpty(subject)) {
            where.append(" and c_subject like ?");
            args.add(dataSource.getDialect(null).getLike(subject, true, true));
        }

        return liteOrm.query(new LiteQuery(DocModel.class).where(where.toString()).order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    private void append(StringBuilder where, List<Object> args, String name, String value) {
        if (!validator.isEmpty(value)) {
            where.append(" and c_").append(name).append("=?");
            args.add(value);
        }
    }

    @Override
    public PageList<DocModel> queryByAuthor(String author, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(DocModel.class).where(Recycle.No.getSql() + " and c_author=?").order("c_time desc").size(pageSize).page(pageNum), new Object[]{author});
    }

    @Override
    public void save(DocModel doc) {
        liteOrm.save(doc);
    }
}
