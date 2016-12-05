package org.lpw.ranch.audit;

import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(TestAuditModel.NAME + ".service")
public class TestAuditServiceImpl implements TestAuditService {
    private String[] passIds;
    private String[] refuseIds;

    @Override
    public String[] getPassIds() {
        return passIds;
    }

    @Override
    public String[] getRefuseIds() {
        return refuseIds;
    }

    @Override
    public void pass(String[] ids) {
        passIds = ids;
    }

    @Override
    public void refuse(String[] ids) {
        refuseIds = ids;
    }
}
