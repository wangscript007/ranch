package org.lpw.ranch.doc;

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
public class FavoriteTest extends TestSupport {
    @Test
    public void favorite() {
        List<DocModel> list = create(2);

        mockHelper.reset();
        mockHelper.mock("/doc/favorite");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1491, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1412, object.getInt("code"));
        Assert.assertEquals(message.get(DocModel.NAME + ".id.not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(500 + i, findById(list.get(i).getId()).getFavorite());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(0).getId());
        mockHelper.getRequest().addParameter("favorite", "5");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(505, findById(list.get(0).getId()).getFavorite());
        Assert.assertEquals(501, findById(list.get(1).getId()).getFavorite());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(1).getId());
        mockHelper.getRequest().addParameter("favorite", "-5");
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/doc/favorite");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        Assert.assertEquals(505, findById(list.get(0).getId()).getFavorite());
        Assert.assertEquals(496, findById(list.get(1).getId()).getFavorite());
    }
}
