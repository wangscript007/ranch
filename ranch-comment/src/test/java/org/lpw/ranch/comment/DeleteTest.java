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
public class DeleteTest extends TestSupport {
    @Test
    public void delete() {
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, "owner " + i, "author " + (i % 2), Audit.values()[i % 3]));

        mockHelper.reset();
        mockHelper.mock("/comment/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1311, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/comment/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1311, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(CommentModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/comment/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getInt("code"));
        Assert.assertEquals(message.get("ranch.base.user.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockUser.sign("author 0");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/comment/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1312, object.getInt("code"));
        Assert.assertEquals(message.get(CommentModel.NAME + ".delete.disable"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        mockHelper.mock("/comment/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1312, object.getInt("code"));
        Assert.assertEquals(message.get(CommentModel.NAME + ".delete.disable"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(3).getId());
        mockHelper.mock("/comment/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1312, object.getInt("code"));
        Assert.assertEquals(message.get(CommentModel.NAME + ".delete.disable"), object.getString("message"));
        list.forEach(comment -> Assert.assertNotNull(liteOrm.findById(CommentModel.class, comment.getId())));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.mock("/comment/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertNull(liteOrm.findById(CommentModel.class, list.get(0).getId()));
        for (int i = 1; i < list.size(); i++)
            Assert.assertNotNull(liteOrm.findById(CommentModel.class, list.get(i).getId()));
    }
}
