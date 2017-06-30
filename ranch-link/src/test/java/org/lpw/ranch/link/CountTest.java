package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.test.PageTester;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class CountTest extends TestSupport {
    @Test
    public void count() {
        for (int i = 0; i < 10; i++)
            create(i, 0, 1, i);
        for (int i = 0; i < 10; i++)
            create(i, 1, i, 1);

        mockHelper.reset();
        mockHelper.mock("/link/count");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertEquals(0,object.getIntValue("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.getRequest().addParameter("id1", "id1 0");
        mockHelper.mock("/link/count");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertEquals(0,object.getIntValue("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 0");
        mockHelper.getRequest().addParameter("id1", "id1 1");
        mockHelper.mock("/link/count");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertEquals(10,object.getIntValue("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("id2", "id2 1");
        mockHelper.mock("/link/count");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertEquals(10,object.getIntValue("data"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "type 1");
        mockHelper.getRequest().addParameter("id1", "id1 1");
        mockHelper.getRequest().addParameter("id2", "id2 2");
        mockHelper.mock("/link/count");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.get("code"));
        Assert.assertEquals(1,object.getIntValue("data"));
    }
}
