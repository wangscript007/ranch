package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.List;

/**
 * @author lpw
 */
public class PraiseTest extends TestSupport {
    @Test
    public void comment() {
        schedulerAspect.pause();
        List<DocModel> list = create(2);

        mockHelper.reset();
        mockHelper.mock("/doc/praise");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/doc/praise");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        mockHelper.mock("/doc/praise");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1412, object.getIntValue("code"));
        Assert.assertEquals(message.get(DocModel.NAME + ".id.not-exists"), object.getString("message"));

        praise(list, 0);
        praise(list, 0);
        praise(list, 1);
        praise(list, 1);

        for (int i = 0; i < 2; i++) {
            ((DocServiceImpl) docService).executeMinuteJob();
            for (int j = 0; j < list.size(); j++)
                Assert.assertEquals(802 + j, findById(list.get(j).getId()).getPraise());
        }

        schedulerAspect.press();
    }

    private void praise(List<DocModel> list, int index) {
        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", list.get(index).getId());
        mockHelper.mock("/doc/praise");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        for (int i = 0; i < list.size(); i++)
            Assert.assertEquals(800 + i, findById(list.get(i).getId()).getPraise());

    }
}
