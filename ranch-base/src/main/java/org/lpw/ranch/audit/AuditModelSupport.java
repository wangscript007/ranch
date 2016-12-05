package org.lpw.ranch.audit;

import org.lpw.tephra.dao.model.Jsonable;
import org.lpw.tephra.dao.model.ModelSupport;

import javax.persistence.Column;

/**
 * @author lpw
 */
public class AuditModelSupport extends ModelSupport implements AuditModel {
    private int audit; // 审核：0-待审核；1-审核通过；2-审核不通过

    @Jsonable
    @Column(name = "c_audit")
    @Override
    public int getAudit() {
        return audit;
    }

    @Override
    public void setAudit(int audit) {
        this.audit = audit;
    }
}
