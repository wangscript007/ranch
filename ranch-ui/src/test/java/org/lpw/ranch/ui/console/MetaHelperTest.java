package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class MetaHelperTest extends TephraTestSupport {
    @Inject
    private MetaHelper metaHelper;

    //    @Test
    public void get() {
        Assert.assertNull(metaHelper.get("ranch.util.meta1"));
        Assert.assertNull(metaHelper.get("ranch.util.meta2"));

        JSONObject object = metaHelper.get("ranch.util.meta");
        Assert.assertEquals("ranch.util.meta", object.getString("key"));
    }
}
