package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.ctrl.validate.Validators;
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
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".format"), 0, 7), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "8");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1803, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-between", message.get(MessageModel.NAME + ".format"), 0, 7), object.getString("message"));

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
        mockHelper.getRequest().addParameter("content", generator.random(1001));
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1805, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "over-max-length", message.get(MessageModel.NAME + ".content"), 1000), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1806, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-match-regex", message.get(MessageModel.NAME + ".code"), "^[a-zA-Z0-9]{1,64}$"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.getRequest().addParameter("code", "code value");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(1806, object.getIntValue("code"));
        Assert.assertEquals(message.get(Validators.PREFIX + "not-match-regex", message.get(MessageModel.NAME + ".code"), "^[a-zA-Z0-9]{1,64}$"), object.getString("message"));

        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", generator.uuid());
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "content value");
        mockHelper.getRequest().addParameter("code", "codevalue");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(9901, object.getIntValue("code"));
        Assert.assertEquals(message.get("ranch.user.helper.need-sign-in"), object.getString("message"));

        for (int i = 0; i < 2; i++) {
            String receiver = generator.uuid();
            mockCarousel.register("ranch.user.sign", "{\"code\":0,\"data\":{\"id\":\"sign in id\"}}");
            mockHelper.reset();
            mockHelper.getRequest().addParameter("type", "" + i);
            mockHelper.getRequest().addParameter("receiver", receiver);
            mockHelper.getRequest().addParameter("format", "1");
            mockHelper.getRequest().addParameter("content", "content value");
            mockHelper.mock("/message/send");
            object = mockHelper.getResponse().asJson();
            Assert.assertEquals(1805, object.getIntValue("code"));
            Assert.assertEquals(message.get(MessageModel.NAME + ".send.failure"), object.getString("message"));
        }

        String receiver = generator.uuid();
        mockCarousel.register("ranch.friend.get", "{\"code\":0,\"data\":{\"" + receiver + "\":{\"id\":\"" + receiver + "\",\"user\":\"friend user id\"}}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "0");
        mockHelper.getRequest().addParameter("receiver", receiver);
        mockHelper.getRequest().addParameter("format", "1");
        mockHelper.getRequest().addParameter("content", "friend content");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        MessageModel message = liteOrm.findOne(new LiteQuery(MessageModel.class).where("c_type=?"), new Object[]{0});
        Assert.assertEquals("sign in id", message.getSender());
        Assert.assertEquals(0, message.getType());
        Assert.assertEquals("friend user id", message.getReceiver());
        Assert.assertEquals(1, message.getFormat());
        Assert.assertEquals("friend content", message.getContent());
        Assert.assertTrue(System.currentTimeMillis() - message.getTime().getTime() < 2000L);

        receiver = generator.uuid();
        mockCarousel.register("ranch.group.member.find", "{\"code\":0,\"data\":{\"id\":\"member id\"}}");
        mockHelper.reset();
        mockHelper.getRequest().addParameter("type", "1");
        mockHelper.getRequest().addParameter("receiver", receiver);
        mockHelper.getRequest().addParameter("format", "2");
        mockHelper.getRequest().addParameter("content", "group content");
        mockHelper.mock("/message/send");
        object = mockHelper.getResponse().asJson();
        Assert.assertEquals(0, object.getIntValue("code"));
        Assert.assertEquals("", object.getString("data"));
        message = liteOrm.findOne(new LiteQuery(MessageModel.class).where("c_type=?"), new Object[]{1});
        Assert.assertEquals("member id", message.getSender());
        Assert.assertEquals(1, message.getType());
        Assert.assertEquals(receiver, message.getReceiver());
        Assert.assertEquals(2, message.getFormat());
        Assert.assertEquals("group content", message.getContent());
        Assert.assertTrue(System.currentTimeMillis() - message.getTime().getTime() < 2000L);
    }
}
