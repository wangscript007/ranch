package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class SmtpSenderTest extends TephraTestSupport {
    @Inject
    private PushService pushService;

    //    @Test
    public void send() {
        pushService.save(null, "test.smtp", "smtp", "app-code", "Hello ${data.name}",
                "Nice to meet you ${data.name}", null, "ranch-push", 1);
        JSONObject args = new JSONObject();
        args.put("name", "smtp");
        Assert.assertTrue(pushService.send("test.smtp","", "e@mail", args));
    }
}
