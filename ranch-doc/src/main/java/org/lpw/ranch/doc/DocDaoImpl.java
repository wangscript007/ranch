package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditDao;
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
    @Inject
    private AuditDao auditDao;
    private Map<String, String> map = new ConcurrentHashMap<>();

    @Override
    public DocModel findById(String id) {
        return liteOrm.findById(DocModel.class, id);
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

    @Override
    public void audit(String[] ids, Audit audit, String auditRemark) {
        auditDao.audit(DocModel.class, ids, audit, auditRemark);
    }
}
