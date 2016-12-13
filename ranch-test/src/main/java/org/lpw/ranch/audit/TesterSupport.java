package org.lpw.ranch.audit;

import org.junit.Assert;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lpw
 */
public class TesterSupport {
    @Autowired
    protected Message message;
    @Autowired
    protected Generator generator;
    @Autowired
    protected Sign sign;
    @Autowired
    protected MockHelper mockHelper;

    protected <T extends AuditModel> void equals(AuditTesterDao<T> testerDao, List<T> list, int i) {
        for (; i < list.size(); i++) {
            T model = testerDao.findById(list.get(i).getId());
            Assert.assertEquals(Audit.values()[i % 3].getValue(), model.getAudit());
            Assert.assertEquals("remark " + i, model.getAuditRemark());
        }
    }
}
