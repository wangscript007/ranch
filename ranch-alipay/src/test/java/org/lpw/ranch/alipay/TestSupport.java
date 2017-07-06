package org.lpw.ranch.alipay;

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

    AlipayModel create(int i) {
        return create(i, "app id " + i);
    }

    AlipayModel create(int i, String appId) {
        AlipayModel alipay = new AlipayModel();
        alipay.setKey("key " + i);
        alipay.setName("name " + i);
        alipay.setAppId(appId);
        alipay.setPrivateKey("private key " + i);
        alipay.setPublicKey("public key " + i);
        liteOrm.save(alipay);

        return alipay;
    }

    void equals(JSONObject object, int i) {
        Assert.assertEquals("key " + i, object.getString("key"));
        Assert.assertEquals("name " + i, object.getString("name"));
        Assert.assertEquals("app id " + i, object.getString("appId"));
        Assert.assertEquals("private key " + i, object.getString("privateKey"));
        Assert.assertEquals("public key " + i, object.getString("publicKey"));
    }
}
