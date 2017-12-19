package org.lpw.ranch.logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.PageTester;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class LoggerCtrlTest extends TephraTestSupport {
    @Inject
    private Message message;
    @Inject
    private Sign sign;
    @Inject
    private DateTime dateTime;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private MockHelper mockHelper;
    @Inject
    private PageTester pageTester;

    @Test
    public void query() {
        List<LoggerModel> list = new ArrayList<>();
        long time = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            LoggerModel logger = new LoggerModel();
            logger.setKey("key " + (i % 2));
            logger.setTime(new Timestamp(time - i * TimeUnit.Minute.getTime()));
            liteOrm.save(logger);
            list.add(logger);
        }

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        mockHelper.mock("/logger/query");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9995, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/logger/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(0, 20, 1, data);
        Assert.assertTrue(data.getJSONArray("list").isEmpty());

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 1");
        mockHelper.getRequest().addParameter("pageSize", "2");
        mockHelper.getRequest().addParameter("pageNum", "2");
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/logger/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(5, 2, 2, data);
        JSONArray array = data.getJSONArray("list");
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(list.get(5).getId(), array.getJSONObject(0).getString("id"));
        Assert.assertEquals(list.get(7).getId(), array.getJSONObject(1).getString("id"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("key", "key 0");
        mockHelper.getRequest().addParameter("start", dateTime.toString(new Timestamp(time - 7 * TimeUnit.Minute.getTime())));
        mockHelper.getRequest().addParameter("end", dateTime.toString(new Timestamp(time - 3 * TimeUnit.Minute.getTime())));
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/logger/query");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        pageTester.assertCountSizeNumber(2, 20, 1, data);
        array = data.getJSONArray("list");
        Assert.assertEquals(2, array.size());
        Assert.assertEquals(list.get(4).getId(), array.getJSONObject(0).getString("id"));
        Assert.assertEquals(list.get(6).getId(), array.getJSONObject(1).getString("id"));
    }
}
