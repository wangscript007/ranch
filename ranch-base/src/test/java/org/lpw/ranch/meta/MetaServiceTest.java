package org.lpw.ranch.meta;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class MetaServiceTest extends TephraTestSupport {
    @Inject
    private MetaService metaService;

//    @Test
    public void get() {
        Assert.assertNull(metaService.get(null));
        Assert.assertNull(metaService.get(""));
        Assert.assertNull(metaService.get(MetaModel.NAME));
        Assert.assertNull(metaService.get(MetaModel.NAME + ".not-exists"));
        Assert.assertNull(metaService.get(MetaModel.NAME + ".not-json"));
        Assert.assertNull(metaService.get("not-exists2"));
        Assert.assertNull(metaService.get("not-json2"));

        JSONObject object = metaService.get(MetaModel.NAME + ".query");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("query", object.getString("name"));
        JSONArray cols = object.getJSONArray("cols");
        Assert.assertEquals(2, cols.size());
        JSONObject col = cols.getJSONObject(0);
        Assert.assertEquals(2, col.size());
        Assert.assertEquals("key", col.getString("name"));
        Assert.assertEquals("Key label", col.getString("label"));
        col = cols.getJSONObject(1);
        Assert.assertEquals(2, col.size());
        Assert.assertEquals("value", col.getString("name"));
        Assert.assertEquals("Value label", col.getString("label"));

        object = metaService.get("query2");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("query", object.getString("name"));
        cols = object.getJSONArray("cols");
        Assert.assertEquals(2, cols.size());
        col = cols.getJSONObject(0);
        Assert.assertEquals(2, col.size());
        Assert.assertEquals("key", col.getString("name"));
        Assert.assertEquals("key", col.getString("label"));
        col = cols.getJSONObject(1);
        Assert.assertEquals(2, col.size());
        Assert.assertEquals("value", col.getString("name"));
        Assert.assertEquals("Value label", col.getString("label"));

        object = metaService.get(MetaModel.NAME + ".col");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("key", object.getString("name"));
        Assert.assertEquals("ranch.meta.key", object.getString("label"));

        new MetaModel();
    }
}
