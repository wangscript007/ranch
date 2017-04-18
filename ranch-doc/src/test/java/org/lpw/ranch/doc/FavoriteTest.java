package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.List;

/**
 * @author lpw
 */
public class FavoriteTest extends TestSupport {
    @Test
    public void favorite() {
        List<DocModel> list = create(2);

        mockHelper.reset();
        mockHelper.mock("/doc/favorite");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1412, object.getIntValue("code"));
        Assert.assertEquals(message.get(DocModel.NAME + ".id.not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(500 + i, findById(list.get(i).getId()).getFavorite());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("favorite", "5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(505, findById(list.get(0).getId()).getFavorite());
        Assert.assertEquals(501, findById(list.get(1).getId()).getFavorite());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        mockHelper.getRequest().addParameter("favorite", "-5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(505, findById(list.get(0).getId()).getFavorite());
        Assert.assertEquals(496, findById(list.get(1).getId()).getFavorite());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        mockHelper.getRequest().addParameter("favorite", "-500");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(505, findById(list.get(0).getId()).getFavorite());
        Assert.assertEquals(0, findById(list.get(1).getId()).getFavorite());
    }
}
