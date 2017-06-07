package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.HttpAspect;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.SchedulerAspect;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Digest digest;
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    Converter converter;
    @Inject
    Sign sign;
    @Inject
    LiteOrm liteOrm;
    @Inject
    SchedulerAspect schedulerAspect;
    @Inject
    HttpAspect httpAspect;
    @Inject
    MockHelper mockHelper;

    WeixinModel create(int i) {
        return create(i, "app id " + i);
    }

    WeixinModel create(int i, String appId) {
        WeixinModel weixin = new WeixinModel();
        weixin.setKey("key " + i);
        weixin.setName("name " + i);
        weixin.setAppId(appId);
        weixin.setSecret("secret " + i);
        weixin.setToken("token " + i);
        weixin.setMchId("mch id " + i);
        weixin.setMchKey("mch key " + i);
        weixin.setAccessToken("access token " + i);
        weixin.setJsapiTicket("jsapi ticket " + i);
        weixin.setTime(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Hour.getTime()));
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

    WeixinModel findByAppId(String appId) {
        return liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_app_id=?"), new Object[]{appId});
    }
}
