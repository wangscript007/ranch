package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteQuery;

/**
 * @author lpw
 */
public class SendTest extends TestSupport {
    @Test
    public void send() {
        mockCarousel.reset();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "-1");
        mockHelper.mock("/message/send");
        JSONObject object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1801, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".type"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "2");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1801, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".type"), 0, 1), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1802, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(MessageModel.NAME + ".receiver")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", "receiver id");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1802, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "illegal-id", message.get(MessageModel.NAME + ".receiver")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "-1");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1803, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".format"), 0, 5), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "6");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1803, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".format"), 0, 5), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1804, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "empty", message.get(MessageModel.NAME + ".content")), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        String receiver = generator.uuid();
        mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", receiver);
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        PageList<MessageModel> pl = liteOrm.query(new LiteQuery(MessageModel.class), null);
        Assert.assertEquals(1, pl.getList().size());
        MessageModel message = pl.getList().get(0);
        Assert.assertEquals("sign in id", message.getSender());
        Assert.assertEquals(0, message.getType());
        Assert.assertEquals(receiver, message.getReceiver());
        Assert.assertEquals(1, message.getFormat());
        Assert.assertEquals("content value", message.getContent());
        Assert.assertTrue(System.currentTimeMillis() - message.getTime().getTime() < 2000L);
    }
}
