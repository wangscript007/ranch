package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class FindTest extends TestSupport {
    @Test
    public void find() {
        for (int i = 0; i < 10; i++)
            create(i);

        mockHelper.reset();
        mockHelper.mock("/link/find");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2701, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".type")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.mock("/link/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2703, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".id1")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.mock("/link/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2705, object.get("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(LinkModel.NAME + ".id2")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type");
        mockHelper.getRequest().addParameter("id1", "id1");
        mockHelper.getRequest().addParameter("id2", "id2");
        mockHelper.mock("/link/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());

        for (int i = 0; i < 10; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("type", "type " + i);
            mockHelper.getRequest().addParameter("id1", "id1 " + i);
            mockHelper.getRequest().addParameter("id2", "id2 " + i);
            mockHelper.mock("/link/find");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.get("code"));
            equals(object.getJSONObject("data"), i, i, i, i);
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.getRequest().addParameter("id1", "id1 1");
        mockHelper.getRequest().addParameter("id2", "id2 2");
        mockHelper.mock("/link/find");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertTrue(object.getJSONObject("data").isEmpty());
    }
}
