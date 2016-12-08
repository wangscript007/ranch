package org.lpw.ranch.classify;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.test.mock.MockScheduler;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Autowired
    protected Message message;
    @Autowired
    protected Generator generator;
    @Autowired
    protected Converter converter;
    @Autowired
    protected Cache cache;
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected Sign sign;
    @Autowired
    protected MockHelper mockHelper;
    @Autowired
    protected MockScheduler mockScheduler;
    @Autowired
    protected ClassifyService classifyService;

    protected void equalsCodeName(JSONObject object, String code, String name) {
        Assert.assertEquals(code, object.getString("code"));
        Assert.assertEquals(name, object.getString("name"));
    }

    protected ClassifyModel create(int code, boolean recycle) {
        return create(code, "label " + code, recycle);
    }

    protected ClassifyModel create(int code, String label, boolean recycle) {
        ClassifyModel classify = new ClassifyModel();
        classify.setCode("code " + code);
        classify.setName("name " + code);
        classify.setLabel(label);
        classify.setRecycle((recycle ? Recycle.Yes : Recycle.No).getValue());
        liteOrm.save(classify);

        return classify;
    }
}
