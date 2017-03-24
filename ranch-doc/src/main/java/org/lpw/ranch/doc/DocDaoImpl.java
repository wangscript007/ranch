package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Repository(DocModel.NAME + ".dao")
class DocDaoImpl implements DocDao {
    @Inject
    private LiteOrm liteOrm;
    private Map<String, String> map = new ConcurrentHashMap<>();

    @Override
    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
    }

    @Override
    public PageList<DocModel> queryByKey(Audit audit, String key, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(DocModel.class).where(audit.getSql() + " and c_key=?").order("c_time desc").size(pageSize).page(pageNum), new Object[]{key});
    }

    @Override
    public PageList<DocModel> queryByOwner(Audit audit, String owner, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(DocModel.class).where(audit.getSql() + " and c_owner=?").order("c_time desc").size(pageSize).page(pageNum), new Object[]{owner});
    }

    @Override
    public PageList<DocModel> queryByAuthor(Audit audit, String author, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(DocModel.class).where(audit.getSql() + " and c_author=?").order("c_time desc").size(pageSize).page(pageNum), new Object[]{author});
    }

    @Override
    public PageList<DocModel> queryByAuthor(String author, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(DocModel.class).where("c_author=?").order("c_time desc").size(pageSize).page(pageNum), new Object[]{author});
    }

    @Override
    public void save(DocModel doc) {
        liteOrm.save(doc);
    }

    @Override
    public void read(String id, int n) {
        counter(id, "c_read", n);
    }

    @Override
    public void favorite(String id, int n) {
        counter(id, "c_favorite", n);
    }

    @Override
    public void comment(String id, int n) {
        counter(id, "c_comment", n);
    }

    private void counter(String id, String column, int n) {
        String set = map.get(column);
        if (set == null)
            map.put(column, set = column + "=" + column + "+?");
        liteOrm.update(new LiteQuery(DocModel.class).set(set).where("c_id=?"), new Object[]{n, id});
    }
}
