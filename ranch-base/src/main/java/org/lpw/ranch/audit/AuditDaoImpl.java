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
    public <T extends AuditModel> void audit(Class<T> modelClass, String[] ids, Audit audit) {
        if (modelClass == null || validator.isEmpty(ids) || audit == null)
            return;

        StringBuilder where = new StringBuilder("c_id in(");
        List<Object> args = new ArrayList<>();
        for (String id : ids) {
            if (args.size() > 0)
                where.append(',');
            where.append('?');
            args.add(id);
        }

        liteOrm.update(new LiteQuery(modelClass).set(audit.getSql()).where(where.append(')').toString()), args.toArray());
    }
}
