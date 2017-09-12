package org.lpw.ranch.weixin.helper;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class WeixinHelperTest extends TephraTestSupport {
    @Inject
    private WeixinHelper weixinHelper;

    @Test
    public void getId() {
        Assert.assertNull(weixinHelper.getId(null));
        JSONObject object = new JSONObject();
        Assert.assertNull(weixinHelper.getId(object));
        object.put("openid", "open id");
        Assert.assertEquals("open id", weixinHelper.getId(object));
        object.put("unionid", "union id");
        Assert.assertEquals("union id", weixinHelper.getId(object));
    }
}
