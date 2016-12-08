package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class RefreshTest extends TestSupport {
    @Test
    public void refresh() {
        String cacheKey = ClassifyModel.NAME + ".service.random";
        cache.remove(cacheKey);
        mockHelper.reset();
        mockHelper.mock("/classify/refresh");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1291, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-sign"), object.getString("message"));
        Assert.assertNull(cache.get(cacheKey));

        mockHelper.reset();
        sign.put(mockHelper.getRequest().getMap(), null);
        mockHelper.mock("/classify/refresh");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getInt("code"));
        Assert.assertEquals("", object.getString("data"));
        String random = cache.get(cacheKey);
        Assert.assertEquals(32, random.length());

        ((ClassifyServiceImpl) classifyService).executeDateJob();
        String random2 = cache.get(cacheKey);
        Assert.assertEquals(32, random2.length());
        Assert.assertNotEquals(random, random2);
    }
}
