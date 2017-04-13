package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.List;

/**
 * @author lpw
 */
public class SourceTest extends TestSupport {
    @Test
    public void source() {
        List<DocModel> list = create(2);

        mockHelper.reset();
        mockHelper.mock("/doc/source");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/doc/source");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/doc/source");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/doc/source");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1412, object.getIntValue("code"));
        Assert.assertEquals(message.get(DocModel.NAME + ".id.not-exists"), object.getString("message"));

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("id", list.get(i).getId());
            sign.put(mockHelper.getRequest().getMap(), null);
            mockHelper.mock("/doc/source");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(0, object.getIntValue("code"));
            Assert.assertEquals("source " + i, object.getString("data"));
        }
    }
}
