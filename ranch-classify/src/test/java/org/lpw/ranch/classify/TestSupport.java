package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.SchedulerAspect;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.MockHelper;
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

    void equalsCodeName(JSONObject object, String code, String name) {
        Assert.assertEquals(code, object.getString("code"));
        Assert.assertEquals(name, object.getString("name"));
    }

    ClassifyModel create(int code, boolean recycle) {
        return create(code, "label " + code, recycle);
    }

    ClassifyModel create(int code, String label, boolean recycle) {
        ClassifyModel classify = new ClassifyModel();
        classify.setCode("code " + code);
        classify.setName("name " + code);
        classify.setLabel(label);
        classify.setRecycle((recycle ? Recycle.Yes : Recycle.No).getValue());
        liteOrm.save(classify);

        return classify;
    }
}
