package org.lpw.ranch.user.helper;

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
public class ExistsValidatorTest extends TephraTestSupport {
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
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"id value\":{\"id\":\"id value\"}}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/user/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1525, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.not-exists"), object.getString("message"));

        mockCarousel.reset();
        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"id value\":{\"id\":\"id value\",\"name\":\"name value\"}}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id value");
        mockHelper.mock("/user/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(1, data.size());
        Assert.assertEquals("exists", data.getString("state"));
    }
}
