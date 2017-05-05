package org.lpw.ranch.dbtool.schema;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    Sign sign;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockHelper mockHelper;

    SchemaModel create(int i) {
        return create("group " + i, "type " + i, i);
    }

    SchemaModel create(String group, String type, int i) {
        SchemaModel schema = new SchemaModel();
        schema.setGroup(group);
        schema.setKey("key " + i);
        schema.setType(type);
        schema.setIp("ip " + i);
        schema.setName("name " + i);
        schema.setUsername("username " + i);
        schema.setPassword("password " + i);
        schema.setMemo("memo " + i);
        schema.setTables(i);
        liteOrm.save(schema);

        return schema;
    }

    void equals(SchemaModel schema, JSONObject object) {
        Assert.assertEquals(schema.getId(), object.getString("id"));
        Assert.assertEquals(schema.getGroup(), object.getString("group"));
        Assert.assertEquals(schema.getKey(), object.getString("key"));
        Assert.assertEquals(schema.getType(), object.getString("type"));
        Assert.assertEquals(schema.getIp(), object.getString("ip"));
        Assert.assertEquals(schema.getName(), object.getString("name"));
        Assert.assertEquals(schema.getUsername(), object.getString("username"));
        Assert.assertEquals(schema.getPassword(), object.getString("password"));
        Assert.assertEquals(schema.getMemo(), object.getString("memo"));
        Assert.assertEquals(schema.getTables(), object.getIntValue("tables"));
    }
}
