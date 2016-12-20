package org.lpw.ranch.util;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockCarousel;
import org.lpw.tephra.util.Thread;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class CarouselTest extends TephraTestSupport {
    @Autowired private Thread thread;
    @Autowired
    private MockCarousel mockCarousel;
    @Autowired
    private Carousel carousel;

    @Test
    public void get() {
        while ((System.currentTimeMillis()/1000)%60>55)
            thread.sleep(5, TimeUnit.Second);

        mockCarousel.reset();
        JSONObject object = carousel.get("key", "id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("key", "{\"code\":1}");
        object = carousel.get("key", "id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("key", "{\"code\":0,\"data\":{\"name\":\"carousel\"}}");
        object = carousel.get("key", "id 2");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 2", object.getString("id"));

        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 3\":{\"name\":\"carousel\"}}}");
        object = carousel.get("key", "id 3");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("id 3", object.getString("id"));
        Assert.assertEquals("carousel", object.getString("name"));

        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 4\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        object = carousel.get("key", "id 4");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("new id", object.getString("id"));
        Assert.assertEquals("carousel", object.getString("name"));
    }

    @Test
    public void getUser() {
        mockCarousel.reset();
        mockCarousel.register("key", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        JSONObject object = carousel.getUser("id 1");
        Assert.assertEquals(1, object.size());
        Assert.assertEquals("id 1", object.getString("id"));

        mockCarousel.register("ranch.user.get", "{\"code\":0,\"data\":{\"id 1\":{\"id\":\"new id\",\"name\":\"carousel\"}}}");
        object = carousel.getUser("id 1");
        Assert.assertEquals(2, object.size());
        Assert.assertEquals("new id", object.getString("id"));
        Assert.assertEquals("carousel", object.getString("name"));
    }
}
