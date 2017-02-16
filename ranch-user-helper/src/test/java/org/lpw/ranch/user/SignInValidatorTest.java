package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * @author lpw
 */
public class SignInValidatorTest extends TephraTestSupport {
    @Inject
    private Message message;
    @Inject
    private Thread thread;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private MockCarousel mockCarousel;

    @Test
    public void validate() {
        while (Calendar.getInstance().get(Calendar.SECOND) > 55)
            thread.sleep(5, TimeUnit.Second);

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.mock("/user/sign");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user-helper.need-sign-in"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"id\"}}");
        mockHelper.reset();
        mockHelper.mock("/user/sign");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("sign in", data.getString("state"));
    }
}
