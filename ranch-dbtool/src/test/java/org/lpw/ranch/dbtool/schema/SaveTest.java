package org.lpw.ranch.dbtool.schema;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class SaveTest extends TestSupport {
    @Test
    public void save() {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/dbtool/schema/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2301, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(SchemaModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", "group value");
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2302, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(SchemaModel.NAME + ".group")), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2303, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(SchemaModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2304, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(SchemaModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2305, object.getIntValue("code"));
        Assert.assertEquals(message.get(SchemaModel.NAME + ".illegal-type"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2306, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(SchemaModel.NAME + ".ip")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", generator.random(101));
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2307, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(SchemaModel.NAME + ".ip"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2308, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(SchemaModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.getRequest().addParameter("username", generator.random(101));
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2309, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(SchemaModel.NAME + ".username"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.getRequest().addParameter("password", generator.random(101));
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2310, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(SchemaModel.NAME + ".password"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.getRequest().addParameter("memo", generator.random(101));
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2311, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(SchemaModel.NAME + ".memo"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2312, object.getIntValue("code"));
        Assert.assertEquals(message.get(SchemaModel.NAME + ".not-exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("sort", "1");
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.getRequest().addParameter("tables", "5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(6, data.size());
        Assert.assertNotNull(data.getString("id"));
        Assert.assertEquals(1, data.getIntValue("sort"));
        Assert.assertEquals("key value", data.getString("key"));
        Assert.assertEquals("mysql", data.getString("type"));
        Assert.assertEquals("ip value", data.getString("ip"));
        Assert.assertEquals(0, data.getIntValue("tables"));
        SchemaModel schema = liteOrm.findById(SchemaModel.class, data.getString("id"));
        Assert.assertNull(schema.getGroup());
        Assert.assertEquals(1, schema.getSort());
        Assert.assertEquals("key value", schema.getKey());
        Assert.assertEquals("mysql", schema.getType());
        Assert.assertEquals("ip value", schema.getIp());
        Assert.assertNull(schema.getName());
        Assert.assertNull(schema.getUsername());
        Assert.assertNull(schema.getPassword());
        Assert.assertNull(schema.getMemo());
        Assert.assertEquals(0, schema.getTables());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value");
        mockHelper.getRequest().addParameter("tables", "5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2313, object.getIntValue("code"));
        Assert.assertEquals(message.get(SchemaModel.NAME + ".key.exists"), object.getString("message"));


        String group = generator.uuid();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", schema.getId());
        mockHelper.getRequest().addParameter("sort", "2");
        mockHelper.getRequest().addParameter("group", group);
        mockHelper.getRequest().addParameter("key", "key value");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "ip value 1");
        mockHelper.getRequest().addParameter("name", "name value 1");
        mockHelper.getRequest().addParameter("username", "username value 1");
        mockHelper.getRequest().addParameter("password", "password value 1");
        mockHelper.getRequest().addParameter("memo", "memo value 1");
        mockHelper.getRequest().addParameter("tables", "5");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(11, data.size());
        Assert.assertEquals(schema.getId(), data.getString("id"));
        Assert.assertEquals(2, data.getIntValue("sort"));
        Assert.assertEquals(group, data.getString("group"));
        Assert.assertEquals("key value", data.getString("key"));
        Assert.assertEquals("mysql", data.getString("type"));
        Assert.assertEquals("ip value 1", data.getString("ip"));
        Assert.assertEquals("name value 1", data.getString("name"));
        Assert.assertEquals("username value 1", data.getString("username"));
        Assert.assertEquals("password value 1", data.getString("password"));
        Assert.assertEquals("memo value 1", data.getString("memo"));
        Assert.assertEquals(0, data.getIntValue("tables"));
        equals(liteOrm.findById(SchemaModel.class, schema.getId()), data);
    }
}
