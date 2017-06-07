package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class AuthTest extends TestSupport {
    @Inject
    private Logger logger;

    @Test
    @SuppressWarnings({"unchecked"})
    public void auth() {
        schedulerAspect.pause();
        create(0);

        mockHelper.reset();
        mockHelper.mock("/weixin/auth");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2401, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".key")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2451, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(WeixinModel.NAME + ".code")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("code", "code");
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.getRequest().addParameter("code", "code");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2452, object.getIntValue("code"));
        Assert.assertEquals(message.get(WeixinModel.NAME + ".not-exists"), object.getString("message"));

        httpAspect.reset();
        List<String> urls = new ArrayList<>();
        List<Map<String, String>> headers = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> contents = new ArrayList<>();
        contents.add("");
        contents.add("{}");
        contents.add("{\"openid\":\"user open id\"}");
        contents.add("{\"openid\":\"user open id 1\",\"access_token\":\"access token 1\"}");
        contents.add("");
        contents.add("{\"openid\":\"user open id 2\",\"access_token\":\"access token 2\"}");
        contents.add("{\"nickname\":\"user nickname 2\"}");
        httpAspect.get(urls, headers, parameters, contents);

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 0");
        mockHelper.getRequest().addParameter("code", "code 0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());
        code((Map<String, String>) parameters.get(0));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 0");
        mockHelper.getRequest().addParameter("code", "code 0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());
        code((Map<String, String>) parameters.get(1));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 0");
        mockHelper.getRequest().addParameter("code", "code 0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("user open id", data.getString("openid"));
        code((Map<String, String>) parameters.get(2));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 0");
        mockHelper.getRequest().addParameter("code", "code 0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        Assert.assertEquals("user open id 1", data.getString("openid"));
        Assert.assertEquals("access token 1", data.getString("access_token"));
        token((Map<String, String>) parameters.get(3), 1);
        token((Map<String, String>) parameters.get(4), 1);

        logger.isDebugEnable();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 0");
        mockHelper.getRequest().addParameter("code", "code 0");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/weixin/auth");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(3, data.size());
        Assert.assertEquals("user open id 2", data.getString("openid"));
        Assert.assertEquals("access token 2", data.getString("access_token"));
        Assert.assertEquals("user nickname 2", data.getString("nickname"));
        token((Map<String, String>) parameters.get(5), 2);
        token((Map<String, String>) parameters.get(6), 2);
        Assert.assertEquals(7, parameters.size());

        schedulerAspect.press();
    }

    private void code(Map<String, String> parameter) {
        Assert.assertEquals(4, parameter.size());
        Assert.assertEquals("app id 0", parameter.get("appid"));
        Assert.assertEquals("secret 0", parameter.get("secret"));
        Assert.assertEquals("code 0", parameter.get("code"));
        Assert.assertEquals("authorization_code", parameter.get("grant_type"));
    }

    private void token(Map<String, String> parameter, int i) {
        Assert.assertEquals(3, parameter.size());
        Assert.assertEquals("access token " + i, parameter.get("access_token"));
        Assert.assertEquals("user open id " + i, parameter.get("openid"));
        Assert.assertEquals("zh_CN", parameter.get("lang"));
    }
}
