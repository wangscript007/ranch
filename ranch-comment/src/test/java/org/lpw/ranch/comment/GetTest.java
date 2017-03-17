package org.lpw.ranch.comment;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.ctrl.validate.Validators;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
public class GetTest extends TestSupport {
    @Test
    public void get() {
        List<CommentModel> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(create(i, Audit.values()[i % Audit.values().length]));

        mockCarousel.reset();
        mockUser.register();
        for (int i = 0; i < list.size(); i++) {
            mockCarousel.register("key " + i + ".get", "{\n" +
                    "  \"code\":0,\n" +
                    "  \"data\":{\n" +
                    "    \"owner " + i + "\":{\n" +
                    "      \"id\":\"owner " + i + "\",\n" +
                    "      \"key\":\"owner key " + i + "\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
        }

        mockHelper.reset();
        mockHelper.mock("/comment/get");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1309, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(CommentModel.NAME + ".ids")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("ids", "id1," + list.get(0).getId() + ",id2," + list.get(1).getId() + ",id3," + list.get(1).getId() + ",id3");
        mockHelper.mock("/comment/get");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        JSONObject data = object.getJSONObject("data");
        Assert.assertEquals(2, data.size());
        for (int i = 0; i < 2; i++)
            equals(list.get(i), data.getJSONObject(list.get(i).getId()), i);
    }
}
