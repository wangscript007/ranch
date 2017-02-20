package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.SchedulerAspect;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;

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
    Converter converter;
    @Inject
    Cache cache;
    @Inject
    LiteOrm liteOrm;
    @Inject
    Sign sign;
    @Inject
    MockHelper mockHelper;
    @Inject
    SchedulerAspect schedulerAspect;
    @Inject
    ClassifyService classifyService;

    void equalsCodeKeyName(JSONObject object, int i) {
        for (String name : new String[]{"code", "key", "name"})
            Assert.assertEquals(name + " " + i, object.get(name));
    }

    void equalsCodeKeyName(JSONObject object, String code, String key, String name) {
        Assert.assertEquals(code, object.getString("code"));
        if (key == null)
            Assert.assertFalse(object.containsKey("key"));
        else
            Assert.assertEquals(key, object.getString("key"));
        Assert.assertEquals(name, object.getString("name"));
    }

    ClassifyModel create(int code, boolean recycle) {
        return create(code, "{\"json\":" + code + "}", recycle);
    }

    ClassifyModel create(int code, String json, boolean recycle) {
        ClassifyModel classify = new ClassifyModel();
        classify.setCode("code " + code);
        classify.setKey("key " + code);
        classify.setName("name " + code);
        classify.setJson(json);
        classify.setRecycle((recycle ? Recycle.Yes : Recycle.No).getValue());
        liteOrm.save(classify);

        return classify;
    }
}
