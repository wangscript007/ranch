package org.lpw.ranch.weixin;

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
        schedulerAspect.pause();

        mockHelper.reset();
        mockHelper.mock("/weixin/save");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2401, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2402, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".key"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2403, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".name"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2404, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".appId")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2405, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".appId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2406, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".secret")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2407, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".secret"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("token", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2408, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".token"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("mchId", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2409, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".mchId"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.getRequest().addParameter("mchKey", generator.random(101));
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2410, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(WeixinModel.NAME + ".mchKey"), 100), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        WeixinModel weixin1 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key"});
        Assert.assertEquals("key", weixin1.getKey());
        Assert.assertNull(weixin1.getName());
        Assert.assertEquals("app id", weixin1.getAppId());
        Assert.assertEquals("secret", weixin1.getSecret());
        Assert.assertNull(weixin1.getToken());
        Assert.assertNull(weixin1.getMchId());
        Assert.assertNull(weixin1.getMchKey());
        Assert.assertNull(weixin1.getAccessToken());
        Assert.assertNull(weixin1.getJsapiTicket());
        Assert.assertNull(weixin1.getTime());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 2");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2411, object.getIntValue("code"));
        Assert.assertEquals(message.get(WeixinModel.NAME + ".exists"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("appId", "app id");
        mockHelper.getRequest().addParameter("secret", "secret 1");
        mockHelper.getRequest().addParameter("secret", "secret 1");
        mockHelper.getRequest().addParameter("token", "token 1");
        mockHelper.getRequest().addParameter("mchId", "mch id 1");
        mockHelper.getRequest().addParameter("mchKey", "mch key 1");
        mockHelper.getRequest().addParameter("accessToken", "access token 1");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 1");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:05");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        weixin1 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key"});
        Assert.assertEquals("key", weixin1.getKey());
        Assert.assertNull(weixin1.getName());
        Assert.assertEquals("app id", weixin1.getAppId());
        Assert.assertEquals("secret 1", weixin1.getSecret());
        Assert.assertEquals("token 1", weixin1.getToken());
        Assert.assertEquals("mch id 1", weixin1.getMchId());
        Assert.assertEquals("mch key 1", weixin1.getMchKey());
        Assert.assertNull(weixin1.getAccessToken());
        Assert.assertNull(weixin1.getJsapiTicket());
        Assert.assertNull(weixin1.getTime());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("name", "name 2");
        mockHelper.getRequest().addParameter("appId", "app id 2");
        mockHelper.getRequest().addParameter("secret", "secret 2");
        mockHelper.getRequest().addParameter("token", "token 2");
        mockHelper.getRequest().addParameter("mchId", "mch id 2");
        mockHelper.getRequest().addParameter("mchKey", "mch key 2");
        mockHelper.getRequest().addParameter("accessToken", "access token 2");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 2");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:22");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        WeixinModel weixin2 = liteOrm.findById(WeixinModel.class, weixin1.getId());
        Assert.assertEquals("key", weixin2.getKey());
        Assert.assertEquals("name 2", weixin2.getName());
        Assert.assertEquals("app id 2", weixin2.getAppId());
        Assert.assertEquals("secret 2", weixin2.getSecret());
        Assert.assertEquals("token 2", weixin2.getToken());
        Assert.assertEquals("mch id 2", weixin2.getMchId());
        Assert.assertEquals("mch key 2", weixin2.getMchKey());
        Assert.assertNull(weixin2.getAccessToken());
        Assert.assertNull(weixin2.getJsapiTicket());
        Assert.assertNull(weixin2.getTime());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", weixin1.getId());
        mockHelper.getRequest().addParameter("key", "key 3");
        mockHelper.getRequest().addParameter("name", "name 3");
        mockHelper.getRequest().addParameter("appId", "app id 3");
        mockHelper.getRequest().addParameter("secret", "secret 3");
        mockHelper.getRequest().addParameter("token", "token 3");
        mockHelper.getRequest().addParameter("mchId", "mch id 3");
        mockHelper.getRequest().addParameter("mchKey", "mch key 3");
        mockHelper.getRequest().addParameter("accessToken", "access token 3");
        mockHelper.getRequest().addParameter("jsapiTicket", "jsapi ticket 3");
        mockHelper.getRequest().addParameter("time", "2017-01-02 03:04:33");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/save");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        WeixinModel weixin3 = liteOrm.findOne(new LiteQuery(WeixinModel.class).where("c_key=?"), new Object[]{"key 3"});
        Assert.assertNotEquals(weixin1.getId(), weixin3.getId());
        Assert.assertEquals("key 3", weixin3.getKey());
        Assert.assertEquals("name 3", weixin3.getName());
        Assert.assertEquals("app id 3", weixin3.getAppId());
        Assert.assertEquals("secret 3", weixin3.getSecret());
        Assert.assertEquals("token 3", weixin3.getToken());
        Assert.assertEquals("mch id 3", weixin3.getMchId());
        Assert.assertEquals("mch key 3", weixin3.getMchKey());
        Assert.assertNull(weixin3.getAccessToken());
        Assert.assertNull(weixin3.getJsapiTicket());
        Assert.assertNull(weixin3.getTime());

        schedulerAspect.press();
    }
}
