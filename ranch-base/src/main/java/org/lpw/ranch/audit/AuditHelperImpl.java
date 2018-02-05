package org.lpw.ranch.audit;

import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Set;

/**
 * @author lpw
 */
@Component("ranch.audit.helper")
public class AuditHelperImpl implements AuditHelper {
    @Inject
    private Validator validator;
    @Inject
    private AuditDao auditDao;

    @Override
    public Audit get(int value) {
        return value < 0 || value >= Audit.values().length ? null : Audit.values()[value];
    }

    @Override
    public void addProperty(Set<String> set) {
        if (set == null)
            return;

        set.add("audit");
        set.add("auditRemark");
    }

    @Override
    public <T extends AuditModel> void pass(Class<T> modelClass, String[] ids, String auditRemark) {
        audit(modelClass, ids, Audit.Pass, auditRemark);
    }

    @Override
    public <T extends AuditModel> void reject(Class<T> modelClass, String[] ids, String auditRemark) {
        audit(modelClass, ids, Audit.Reject, auditRemark);
    }

    private <T extends AuditModel> void audit(Class<T> modelClass, String[] ids, Audit audit, String auditRemark) {
        if (modelClass == null || validator.isEmpty(ids))
            return;

        auditDao.audit(modelClass, ids, audit, auditRemark);
    }
}
