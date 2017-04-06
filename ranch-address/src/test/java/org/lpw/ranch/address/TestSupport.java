package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;
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
    DateTime dateTime;
    @Inject
    Generator generator;
    @Inject
    Thread thread;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockHelper mockHelper;
    long time = System.currentTimeMillis() / 60 / 1000 * 60 * 1000;

    AddressModel create(int i) {
        return create("user " + i, i, i);
    }

    AddressModel create(String user, int i, int major) {
        AddressModel address = new AddressModel();
        address.setUser(user);
        address.setRegion("region " + i);
        address.setDetail("detail " + i);
        address.setPostcode("postcode " + i);
        address.setLatitude("latitude " + i);
        address.setLongitude("longitude " + i);
        address.setLabel("label " + i);
        address.setMajor(major);
        address.setTime(new Timestamp(time - i * TimeUnit.Hour.getTime()));
        liteOrm.save(address);

        return address;
    }

    void equals(JSONObject object, String user, int i, int major, boolean time) {
        Assert.assertEquals(user, object.getString("user"));
        Assert.assertEquals("region " + i, object.getJSONObject("region").getString("id"));
        Assert.assertEquals("detail " + i, object.getString("detail"));
        Assert.assertEquals("postcode " + i, object.getString("postcode"));
        Assert.assertEquals("latitude " + i, object.getString("latitude"));
        Assert.assertEquals("longitude " + i, object.getString("longitude"));
        Assert.assertEquals("label " + i, object.getString("label"));
        Assert.assertEquals(major, object.getIntValue("major"));
        if (time)
            Assert.assertEquals(dateTime.toString(new Timestamp(this.time - i * TimeUnit.Hour.getTime())), object.getString("time"));
    }

    void equals(AddressModel address, String user, int i, int major, boolean time) {
        Assert.assertEquals(user, address.getUser());
        Assert.assertEquals("region " + i, address.getRegion());
        Assert.assertEquals("detail " + i, address.getDetail());
        Assert.assertEquals("postcode " + i, address.getPostcode());
        Assert.assertEquals("latitude " + i, address.getLatitude());
        Assert.assertEquals("longitude " + i, address.getLongitude());
        Assert.assertEquals("label " + i, address.getLabel());
        Assert.assertEquals(major, address.getMajor());
        if (time)
            Assert.assertEquals(dateTime.toString(new Timestamp(this.time - i * TimeUnit.Hour.getTime())), dateTime.toString(address.getTime()));
    }
}
