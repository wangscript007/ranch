package org.lpw.ranch.doc;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
public class ReadTest extends TestSupport {
    @Test
    @SuppressWarnings({"unchecked"})
    public void read() {
        mockScheduler.pause();
        List<DocModel> list = create(2);

        mockHelper.reset();
        mockHelper.mock("/doc/read");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", "id");
        mockHelper.mock("/doc/read");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1411, object.getInt("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(DocModel.NAME + ".id")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("id", generator.uuid());
        request.putSign(mockHelper.getRequest().getMap());
        mockHelper.mock("/doc/read");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1412, object.getInt("code"));
        Assert.assertEquals(message.get(DocModel.NAME + ".id.not-exists"), object.getString("message"));

        for (int i = 0; i < 2; i++) {
            mockHelper.reset();
            mockHelper.getRequest().addParameter("id", list.get(0).getId());
            mockHelper.mock("/doc/read", true);
            Assert.assertEquals("/doc/read", mockHelper.getFreemarker().getName());
            Map<String, Object> map = (Map<String, Object>) mockHelper.getFreemarker().getData();
            Assert.assertEquals(2, map.size());
            Assert.assertFalse((Boolean) map.get("html"));
            Assert.assertEquals("content 0", map.get("content"));
            for (int j = 0; j < list.size(); j++)
                Assert.assertEquals(400 + j, findById(list.get(j).getId()).getRead());

            mockHelper.reset();
            mockHelper.getRequest().addParameter("id", list.get(1).getId());
            mockHelper.getRequest().addParameter("html", "true");
            mockHelper.getRequest().addParameter("css", "css1,css2");
            mockHelper.mock("/doc/read", true);
            Assert.assertEquals("/doc/read", mockHelper.getFreemarker().getName());
            map = (Map<String, Object>) mockHelper.getFreemarker().getData();
            Assert.assertEquals(4, map.size());
            Assert.assertTrue((Boolean) map.get("html"));
            Assert.assertEquals(list.get(1).getId(), ((DocModel) map.get("model")).getId());
            Assert.assertArrayEquals(new String[]{"css1", "css2"}, (String[]) map.get("css"));
            Assert.assertEquals("content 1", map.get("content"));
            for (int j = 0; j < list.size(); j++)
                Assert.assertEquals(400 + j, findById(list.get(j).getId()).getRead());
        }

        for (int i = 0; i < 2; i++) {
            ((DocServiceImpl) docService).executeMinuteJob();
            for (int j = 0; j < list.size(); j++)
                Assert.assertEquals(402 + j, findById(list.get(j).getId()).getRead());
        }

        mockScheduler.press();
    }
}
