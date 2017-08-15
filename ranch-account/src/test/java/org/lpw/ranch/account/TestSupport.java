package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;

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
    Digest digest;
    @Inject
    Sign sign;
    @Inject
    Thread thread;
    @Inject
    LiteOrm liteOrm;
    @Inject
    LockHelper lockHelper;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockHelper mockHelper;

    void mockUser() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            JSONObject user = new JSONObject();
            String id = parameter.get("ids");
            user.put("id", id);
            if (id.length() <= 36)
                user.put("name", "name " + id);
            data.put(id, user);
            json.put("data", data);

            return json.toJSONString();
        });
    }

    void validate(String action, int code) {
        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("owner", generator.random(101));
        mockHelper.mock("/account/" + action);
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2201, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(AccountModel.NAME + ".owner"), 36), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "-1");
        mockHelper.mock("/account/" + action);
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(AccountModel.NAME + ".type"), 0, 9), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "10");
        mockHelper.mock("/account/" + action);
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2202, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(AccountModel.NAME + ".type"), 0, 9), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.mock("/account/" + action);
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2204, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-greater-than", message.get(AccountModel.NAME + ".amount"), 0), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.mock("/account/" + action);
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2205, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(AccountModel.NAME + ".user")), object.getString("message"));

        mockUser();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.getRequest().addParameter("user", generator.random(37));
        mockHelper.mock("/account/" + action);
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2205, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists-and-not-sign-in", message.get(AccountModel.NAME + ".user")), object.getString("message"));

        String lockId = lockHelper.lock(AccountModel.NAME + ".service.lock:sign in id--0", 1000L);
        mockUser();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("amount", "1");
        mockHelper.mock("/account/" + action);
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(2200 + code, object.getIntValue("code"));
        Assert.assertEquals(message.get(AccountModel.NAME + "." + action + ".failure"), object.getString("message"));
        lockHelper.unlock(lockId);
    }
}
