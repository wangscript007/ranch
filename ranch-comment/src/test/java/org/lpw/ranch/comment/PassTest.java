package org.lpw.ranch.comment;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class PassTest extends TestSupport {
    @Test
    public void pass() {
        clean();
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/comment/pass");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1391, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(Audit.values()[i % 3].getValue(), findById(list.get(i).getId()).getAudit());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", list.get(0).getId() + "," + list.get(2).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/comment/pass");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < 3; i++)
            Assert.assertEquals(Audit.Passed.getValue(), findById(list.get(i).getId()).getAudit());
        for (int i = 3; i < list.size(); i++)
            Assert.assertEquals(Audit.values()[i % 3].getValue(), findById(list.get(i).getId()).getAudit());
    }
}
