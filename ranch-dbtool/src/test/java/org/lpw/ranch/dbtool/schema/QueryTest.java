package org.lpw.ranch.dbtool.schema;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class QueryTest extends TestSupport {
    @Test
    public void query() {
        List<SchemaModel> list = new ArrayList<>();
        String[] group = new String[]{generator.uuid(), generator.uuid()};
        list.add(create(group[0], "mysql", 111));
        list.add(create(group[0], "classify", 121));
        list.add(create(group[1], "mysql", 211));
        list.add(create(group[1], "classify", 221));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", "group id");
        mockHelper.mock("/dbtool/schema/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2302, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(SchemaModel.NAME + ".group")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type value");
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2305, object.getIntValue("code"));
        Assert.assertEquals(message.get(SchemaModel.NAME + ".illegal-type"), object.getString("message"));

        mockHelper.reset();
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONArray data = object.getJSONArray("data");
        Assert.assertEquals(4, data.size());
        int count = 0;
        for (SchemaModel schema : list) {
            for (int i = 0; i < data.size(); i++) {
                JSONObject obj = data.getJSONObject(i);
                if (schema.getId().equals(obj.getString("id"))) {
                    equals(schema, obj);
                    count++;
                }
            }
        }
        Assert.assertEquals(4, count);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group[0]);
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(2, data.size());
        count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < data.size(); j++) {
                JSONObject obj = data.getJSONObject(j);
                if (list.get(i).getId().equals(obj.getString("id"))) {
                    equals(list.get(i), obj);
                    count++;
                }
            }
        }
        Assert.assertEquals(2, count);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group[0]);
        mockHelper.getRequest().addParameter("key", "2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equals(list.get(1), data.getJSONObject(0));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group[0]);
        mockHelper.getRequest().addParameter("type", "mysql");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equals(list.get(0), data.getJSONObject(0));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group[1]);
        mockHelper.getRequest().addParameter("ip", "22");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equals(list.get(3), data.getJSONObject(0));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group[1]);
        mockHelper.getRequest().addParameter("name", "22");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(1, data.size());
        equals(list.get(3), data.getJSONObject(0));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("group", group[0]);
        mockHelper.getRequest().addParameter("key", "1");
        mockHelper.getRequest().addParameter("type", "mysql");
        mockHelper.getRequest().addParameter("ip", "2");
        mockHelper.getRequest().addParameter("name", "2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/dbtool/schema/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONArray("data");
        Assert.assertEquals(0, data.size());
    }
}
