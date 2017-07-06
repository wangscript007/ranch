package org.lpw.ranch.alipay;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class SaveTest extends TestSupport {
    @Test
    public void save() {
        mockHelper.reset();
        mockHelper.mock("/alipay/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2601, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(AlipayModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2602, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(AlipayModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2603, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(AlipayModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2604, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(AlipayModel.NAME + ".appId")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", generator.random(101));
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2605, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(AlipayModel.NAME + ".appId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2606, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(AlipayModel.NAME + ".privateKey")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("privateKey", "private key");
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2607, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(AlipayModel.NAME + ".publicKey")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("privateKey", "private key");
        mockHelper.getRequest().addParameter("publicKey", "public key");
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("privateKey", "private key");
        mockHelper.getRequest().addParameter("publicKey", "public key");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(5, data.size());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals("app id", data.getString("appId"));
        Assert.assertEquals("private key", data.getString("privateKey"));
        Assert.assertEquals("public key", data.getString("publicKey"));
        AlipayModel alipay1 = liteOrm.findOne(new LiteQuery(AlipayModel.class).where("c_key=?"), new Object[]{"key"});
        Assert.assertEquals("key", alipay1.getKey());
        Assert.assertNull(alipay1.getName());
        Assert.assertEquals("app id", alipay1.getAppId());
        Assert.assertEquals("private key", alipay1.getPrivateKey());
        Assert.assertEquals("public key", alipay1.getPublicKey());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("privateKey", "private key");
        mockHelper.getRequest().addParameter("publicKey", "public key");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2608, object.getIntValue("code"));
        Assert.assertEquals(message.get(AlipayModel.NAME + ".exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("privateKey", "private key 1");
        mockHelper.getRequest().addParameter("publicKey", "public key 1");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(5, data.size());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals("app id", data.getString("appId"));
        Assert.assertEquals("private key 1", data.getString("privateKey"));
        Assert.assertEquals("public key 1", data.getString("publicKey"));
        alipay1 = liteOrm.findOne(new LiteQuery(AlipayModel.class).where("c_key=?"), new Object[]{"key"});
        Assert.assertEquals("key", alipay1.getKey());
        Assert.assertNull(alipay1.getName());
        Assert.assertEquals("app id", alipay1.getAppId());
        Assert.assertEquals("private key 1", alipay1.getPrivateKey());
        Assert.assertEquals("public key 1", alipay1.getPublicKey());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("appId", "app id 2");
        mockHelper.getRequest().addParameter("privateKey", "private key 2");
        mockHelper.getRequest().addParameter("publicKey", "public key 2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(6, data.size());
        Assert.assertEquals("key", data.getString("key"));
        Assert.assertEquals("name 2", data.getString("name"));
        Assert.assertEquals("app id 2", data.getString("appId"));
        Assert.assertEquals("private key 2", data.getString("privateKey"));
        Assert.assertEquals("public key 2", data.getString("publicKey"));
        AlipayModel alipay2 = liteOrm.findById(AlipayModel.class, alipay1.getId());
        Assert.assertEquals("key", alipay2.getKey());
        Assert.assertEquals("name 2", alipay2.getName());
        Assert.assertEquals("app id 2", alipay2.getAppId());
        Assert.assertEquals("private key 2", alipay2.getPrivateKey());
        Assert.assertEquals("public key 2", alipay2.getPublicKey());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", alipay1.getId());
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("appId", "app id 3");
        mockHelper.getRequest().addParameter("privateKey", "private key 3");
        mockHelper.getRequest().addParameter("publicKey", "public key 3");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(6, data.size());
        Assert.assertEquals("key 3", data.getString("key"));
        Assert.assertEquals("name 3", data.getString("name"));
        Assert.assertEquals("app id 3", data.getString("appId"));
        Assert.assertEquals("private key 3", data.getString("privateKey"));
        Assert.assertEquals("public key 3", data.getString("publicKey"));
        AlipayModel alipay3 = liteOrm.findOne(new LiteQuery(AlipayModel.class).where("c_key=?"), new Object[]{"key 3"});
        Assert.assertNotEquals(alipay1.getId(), alipay3.getId());
        Assert.assertEquals("key 3", alipay3.getKey());
        Assert.assertEquals("name 3", alipay3.getName());
        Assert.assertEquals("app id 3", alipay3.getAppId());
        Assert.assertEquals("private key 3", alipay3.getPrivateKey());
        Assert.assertEquals("public key 3", alipay3.getPublicKey());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", alipay1.getId());
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("name", "name 4");
        mockHelper.getRequest().addParameter("appId", "app id 3");
        mockHelper.getRequest().addParameter("privateKey", "private key 4");
        mockHelper.getRequest().addParameter("publicKey", "public key 4");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/alipay/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(6, data.size());
        Assert.assertEquals("key 3", data.getString("key"));
        Assert.assertEquals("name 4", data.getString("name"));
        Assert.assertEquals("app id 3", data.getString("appId"));
        Assert.assertEquals("private key 4", data.getString("privateKey"));
        Assert.assertEquals("public key 4", data.getString("publicKey"));
        AlipayModel alipay4 = liteOrm.findOne(new LiteQuery(AlipayModel.class).where("c_key=?"), new Object[]{"key 3"});
        Assert.assertNotEquals(alipay1.getId(), alipay4.getId());
        Assert.assertEquals("key 3", alipay4.getKey());
        Assert.assertEquals("name 4", alipay4.getName());
        Assert.assertEquals("app id 3", alipay4.getAppId());
        Assert.assertEquals("private key 4", alipay4.getPrivateKey());
        Assert.assertEquals("public key 4", alipay4.getPublicKey());
    }
}
