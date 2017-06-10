package org.lpw.ranch.account.log;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.PageTester;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    Sign sign;
    @Inject
    DateTime dateTime;
    @Inject
    LiteOrm liteOrm;
    @Inject
    PageTester pageTester;
    @Inject
    MockHelper mockHelper;
    @Inject
    MockCarousel mockCarousel;
    long time = System.currentTimeMillis() / 1000 * 1000;

    LogModel create(int i, int state) {
        LogModel log = new LogModel();
        log.setUser("user " + i);
        log.setAccount("account " + i);
        log.setType("type " + i);
        log.setAmount(100 + i);
        log.setBalance(200 + i);
        log.setState(state);
        log.setStart(new Timestamp(time - i * TimeUnit.Hour.getTime()));
        log.setEnd(new Timestamp(time - (i - 1) * TimeUnit.Hour.getTime()));
        log.setJson("{\"label\":\"label " + i + "\"}");
        log.setIndex(300 + i);
        liteOrm.save(log);

        return log;
    }

    void mockUser() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheTime) -> {
            JSONObject json = new JSONObject();
            json.put("code", 0);
            JSONObject data = new JSONObject();
            JSONObject user = new JSONObject();
            String id = parameter.get("ids");
            user.put("name", "user " + id);
            data.put(id, user);
            json.put("data", data);

            return json.toJSONString();
        });
    }

    void equals(JSONObject object, int i, int state) {
        Assert.assertEquals(10, object.size());
        Assert.assertEquals("user user " + i, object.getJSONObject("user").getString("name"));
        Assert.assertEquals("account " + i, object.getString("account"));
        Assert.assertEquals("type " + i, object.getString("type"));
        Assert.assertEquals(100 + i, object.getIntValue("amount"));
        Assert.assertEquals(200 + i, object.getIntValue("balance"));
        Assert.assertEquals(state, object.getIntValue("state"));
        Assert.assertEquals(dateTime.toString(new Timestamp(time - i * TimeUnit.Hour.getTime())), object.getString("start"));
        Assert.assertEquals(dateTime.toString(new Timestamp(time - (i - 1) * TimeUnit.Hour.getTime())), object.getString("end"));
        Assert.assertEquals("label " + i, object.getString("label"));
    }
}
