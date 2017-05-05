package org.lpw.ranch.dbtool.schema;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class DeleteTest extends TestSupport {
    @Test
    public void delete() {
        SchemaModel schema1 = create(1);
        SchemaModel schema2 = create(2);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/dbtool/schema/delete");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2301, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(SchemaModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/dbtool/schema/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2312, object.getIntValue("code"));
        Assert.assertEquals(message.get(SchemaModel.NAME + ".not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", schema1.getId());
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/delete");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertNull(liteOrm.findById(SchemaModel.class, schema1.getId()));
        Assert.assertNotNull(liteOrm.findById(SchemaModel.class, schema2.getId()));
    }
}
