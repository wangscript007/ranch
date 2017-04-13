package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.validate.Validators;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Test
    public void get() {
        DocModel doc1 = create(1, Audit.Passed);
        DocModel doc2 = create(2, Audit.Passed);
        DocModel doc3 = create(3, Audit.Normal);
        DocModel doc4 = create(4, Audit.Refused);

        mockHelper.reset();
        mockHelper.mock("/doc/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1413, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(DocModel.NAME + ".ids")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1,id2");
        mockHelper.mock("/doc/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertTrue(data.isEmpty());

        mockCarousel.reset();
        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id," + doc1.getId() + "," + doc2.getId() + "," + doc3.getId() + "," + doc4.getId() + "," + doc2.getId() + "," + doc4.getId());
        mockHelper.mock("/doc/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        equals(data.getJSONObject(doc1.getId()), 1, Audit.Passed);
        equals(data.getJSONObject(doc2.getId()), 2, Audit.Passed);
    }
}
