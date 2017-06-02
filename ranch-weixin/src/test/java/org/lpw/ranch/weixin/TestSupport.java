package org.lpw.ranch.weixin;

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

    WeixinModel create(int i) {
        WeixinModel weixin = new WeixinModel();
        weixin.setKey("key " + i);
        weixin.setName("name " + i);
        weixin.setAppId("app id " + i);
        weixin.setSecret("secret " + i);
        weixin.setToken("token " + i);
        weixin.setMchId("mch id " + i);
        weixin.setMchKey("mch key " + i);
        liteOrm.save(weixin);

        return weixin;
    }

    void equals(JSONObject object, int i) {
        Assert.assertEquals("key " + i, object.getString("key"));
        Assert.assertEquals("name " + i, object.getString("name"));
        Assert.assertEquals("app id " + i, object.getString("appId"));
        Assert.assertEquals("secret " + i, object.getString("secret"));
        Assert.assertEquals("token " + i, object.getString("token"));
        Assert.assertEquals("mch id " + i, object.getString("mchId"));
        Assert.assertEquals("mch key " + i, object.getString("mchKey"));
    }
}
