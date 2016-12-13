package org.lpw.ranch.audit;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Component("ranch.audit.tester.refuse")
public class RefuseTesterImpl extends TesterSupport implements RefuseTester {
    @Override
    public <T extends AuditModel> void refuse(AuditTesterDao<T> testerDao, String key, String name, int code) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(testerDao.create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.mock("/" + name + "/refuse");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(code * 100 + 81, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(key + ".ids")), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.mock("/" + name + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(code * 100 + 82, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(key + ".auditRemark")), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.getRequest().addParameter("auditRemark", generator.random(101));
        mockHelper.mock("/" + name + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(code * 100 + 83, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(key + ".auditRemark"), 100), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.getRequest().addParameter("auditRemark", "audit remark");
        mockHelper.mock("/" + name + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(code * 100 + 91, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId() + "," + list.get(1).getId());
        mockHelper.getRequest().addParameter("auditRemark", "audit remark");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/" + name + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < 3; i++) {
            T model = testerDao.findById(list.get(i).getId());
            Assert.assertEquals(Audit.Refused.getValue(), model.getAudit());
            if (i == 2)
                Assert.assertEquals("remark " + i, model.getAuditRemark());
            else
                Assert.assertEquals("audit remark", model.getAuditRemark());
        }
        equals(testerDao, list, 3);
    }
}
