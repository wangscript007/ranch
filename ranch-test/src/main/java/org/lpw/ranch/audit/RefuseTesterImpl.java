package org.lpw.ranch.audit;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Component("ranch.audit.tester.refuse")
public class RefuseTesterImpl implements RefuseTester {
    @Autowired
    protected Message message;
    @Autowired
    protected Request request;
    @Autowired
    protected MockHelper mockHelper;

    @Override
    public <T extends AuditModel> void refuse(AuditTesterDao<T> testerDao, String name, int code) {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(testerDao.create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/" + name + "/refuse");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(code * 100 + 91, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/" + name + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(Audit.values()[i % 3].getValue(), testerDao.findById(list.get(i).getId()).getAudit());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId() + "," + list.get(1).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/" + name + "/refuse");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < 3; i++)
            Assert.assertEquals(Audit.Refused.getValue(), testerDao.findById(list.get(i).getId()).getAudit());
        for (int i = 3; i < list.size(); i++)
            Assert.assertEquals(Audit.values()[i % 3].getValue(), testerDao.findById(list.get(i).getId()).getAudit());
    }
}
