package org.lpw.ranch.meta;

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

    @Test
    public void get() {
        Assert.assertNull(metaService.get(null));
        Assert.assertNull(metaService.get(""));
        Assert.assertNull(metaService.get(MetaModel.NAME));
        JSONObject object = metaService.get(MetaModel.NAME + ".query");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("query", object.getString("name"));
    }
}
