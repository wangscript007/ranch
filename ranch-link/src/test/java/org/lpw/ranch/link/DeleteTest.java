package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class DeleteTest extends TestSupport {
    @Test
    public void delete() {
        List<LinkModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i));

        mockHelper.reset();
        mockHelper.mock("/link/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2701, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.mock("/link/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2703, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".id1")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.mock("/link/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2705, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".id2")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.getRequest().addParameter("id2", "id2");
        mockHelper.mock("/link/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertEquals("", object.getString("data"));
        for (LinkModel link : list)
            Assert.assertNotNull(liteOrm.findById(LinkModel.class, link.getId()));

        for (int i = 0; i < 10; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("type", "type " + i);
            mockHelper.getRequest().addParameter("id1", "id1 " + i);
            mockHelper.getRequest().addParameter("id2", "id2 " + i);
            mockHelper.mock("/link/delete");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.get("code"));
            Assert.assertEquals("", object.getString("data"));
            Assert.assertEquals(9 - i, liteOrm.count(new LiteQuery(LinkModel.class), null));
            for (int j = i + 1; j < 10; j++)
                Assert.assertNotNull(liteOrm.findById(LinkModel.class, list.get(j).getId()));
        }
    }
}
