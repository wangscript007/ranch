package org.lpw.ranch.audit;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository("ranch.audit.dao")
public class AuditDaoImpl implements AuditDao {
    @Autowired
    protected Validator validator;
    @Autowired
    protected LiteOrm liteOrm;

    @Override
    public <T extends AuditModel> void audit(Class<T> modelClass, String[] ids, Audit audit, String auditRemark) {
        if (modelClass == null || validator.isEmpty(ids) || audit == null)
            return;

        List<Object> args = new ArrayList<>();
        StringBuilder set = new StringBuilder(audit.getSql());
        if (!validator.isEmpty(auditRemark)) {
            set.append(",c_audit_remark=?");
            args.add(auditRemark);
        }

        StringBuilder where = new StringBuilder("c_id in(");
        for (int i = 0; i < ids.length; i++) {
            if (i > 0)
                where.append(',');
            where.append('?');
            args.add(ids[i]);
        }

        liteOrm.update(new LiteQuery(modelClass).set(set.toString()).where(where.append(')').toString()), args.toArray());
    }
}
