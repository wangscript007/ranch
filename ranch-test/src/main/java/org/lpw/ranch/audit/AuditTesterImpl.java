package org.lpw.ranch.audit;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.recycle.RecycleTester;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Component("ranch.audit.tester")
public class AuditTesterImpl implements AuditTester {
    @Inject
    protected Message message;
    @Inject
    protected Generator generator;
    @Inject
    protected Sign sign;
    @Inject
    protected MockHelper mockHelper;
    @Inject
    private RecycleTester recycleTester;

    @Override
    public <T extends AuditModel> void all(AuditTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix) {
        pass(testerDao, name, uriPrefix, codePrefix);
        refuse(testerDao, name, uriPrefix, codePrefix);
        recycleTester.all(testerDao, name, uriPrefix, codePrefix);
    }

    @Override
    public <T extends AuditModel> void pass(AuditTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(testerDao.create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.mock("/" + uriPrefix + "/pass");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 81, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(name + ".ids")), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.getRequest().addParameter("auditRemark", generator.random(101));
        mockHelper.mock("/" + uriPrefix + "/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 83, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(name + ".auditRemark"), 100), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.getRequest().addParameter("auditRemark", "audit remark");
        mockHelper.mock("/" + uriPrefix + "/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId() + "," + list.get(2).getId());
        mockHelper.getRequest().addParameter("auditRemark", "audit remark");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/" + uriPrefix + "/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < 3; i++) {
            T model = testerDao.findById(list.get(i).getId());
            Assert.assertEquals(Audit.Passed.getValue(), model.getAudit());
            if (i == 1)
                Assert.assertEquals("remark " + i, model.getAuditRemark());
            else
                Assert.assertEquals("audit remark", model.getAuditRemark());
        }
        equals(testerDao, list, 3);
    }

    @Override
    public <T extends AuditModel> void refuse(AuditTesterDao<T> testerDao, String name, String uriPrefix, int codePrefix) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(testerDao.create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.mock("/" + uriPrefix + "/refuse");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 81, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(name + ".ids")), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.mock("/" + uriPrefix + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 82, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(name + ".auditRemark")), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.getRequest().addParameter("auditRemark", generator.random(101));
        mockHelper.mock("/" + uriPrefix + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(codePrefix * 100 + 83, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(name + ".auditRemark"), 100), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId());
        mockHelper.getRequest().addParameter("auditRemark", "audit remark");
        mockHelper.mock("/" + uriPrefix + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        equals(testerDao, list, 0);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId() + "," + list.get(1).getId());
        mockHelper.getRequest().addParameter("auditRemark", "audit remark");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/" + uriPrefix + "/refuse");
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

    protected <T extends AuditModel> void equals(AuditTesterDao<T> testerDao, List<T> list, int i) {
        for (; i < list.size(); i++) {
            T model = testerDao.findById(list.get(i).getId());
            Assert.assertEquals(Audit.values()[i % 3].getValue(), model.getAudit());
            Assert.assertEquals("remark " + i, model.getAuditRemark());
        }
    }
}
