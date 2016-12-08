package org.lpw.ranch.doc;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.List;

/**
 * @author lpw
 */
public class CommentTest extends TestSupport {
    @Test
    public void comment() {
        List<DocModel> list = create(2);

        mockHelper.reset();
        mockHelper.mock("/doc/comment");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/doc/comment");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/doc/comment");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1491, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/comment");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1412, object.getInt("code"));
        Assert.assertEquals(message.get(DocModel.NAME + ".id.not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/comment");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(600 + i, findById(list.get(i).getId()).getComment());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("comment", "5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/comment");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(605, findById(list.get(0).getId()).getComment());
        Assert.assertEquals(601, findById(list.get(1).getId()).getComment());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        mockHelper.getRequest().addParameter("comment", "-5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/comment");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(605, findById(list.get(0).getId()).getComment());
        Assert.assertEquals(596, findById(list.get(1).getId()).getComment());
    }
}
