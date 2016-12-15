package org.lpw.ranch.audit;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author lpw
 */
@Component("ranch.audit.helper")
public class AuditHelperImpl implements AuditHelper {
    @Override
    public void addProperty(Set<String> set) {
        if (set == null)
            return;

        set.add("audit");
        set.add("auditRemark");
    }
}
