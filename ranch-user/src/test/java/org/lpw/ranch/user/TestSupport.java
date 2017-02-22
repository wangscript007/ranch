package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    protected Generator generator;
    @Inject
    protected Message message;
    @Inject
    protected Converter converter;
    @Inject
    protected Digest digest;
    @Inject
    protected LiteOrm liteOrm;
    @Inject
    protected Request request;
    @Inject
    protected MockHelper mockHelper;

    protected UserModel create(int i) {
        return create(i, 10 + i);
    }

    protected UserModel create(int i, int state) {
        return set(new UserModel(), i, state);
    }

    protected UserModel set(UserModel user, int i, int state) {
        user.setPassword(digest.md5(UserModel.NAME + digest.sha1("password " + i + UserModel.NAME)));
        user.setName("name " + i);
        user.setNick("nick " + i);
        user.setMobile("123123456" + (10 + i));
        user.setEmail("email " + i);
        user.setPortrait("portrait " + i);
        user.setGender(i);
        user.setAddress("address " + i);
        user.setBirthday(new Date(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        user.setCode("code " + i);
        user.setRegister(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Hour.getTime()));
        user.setGrade(i);
        user.setState(state);
        liteOrm.save(user);

        return user;
    }

    protected AuthModel createAuth(String user, String uid, int type) {
        AuthModel auth = new AuthModel();
        auth.setUser(user);
        auth.setUid(uid);
        auth.setType(type);
        liteOrm.save(auth);

        return auth;
    }

    protected void equals(UserModel user, JSONObject object) {
        Assert.assertEquals(user.getId(), object.getString("id"));
        Assert.assertFalse(object.containsKey("password"));
        Assert.assertEquals(user.getName(), object.getString("name"));
        Assert.assertEquals(user.getNick(), object.getString("nick"));
        Assert.assertEquals(user.getMobile(), object.getString("mobile"));
        Assert.assertEquals(user.getEmail(), object.getString("email"));
        Assert.assertEquals(user.getPortrait(), object.getString("portrait"));
        Assert.assertEquals(user.getGender(), object.getIntValue("gender"));
        Assert.assertEquals(user.getAddress(), object.getString("address"));
        Assert.assertEquals(converter.toString(user.getBirthday()), object.getString("birthday"));
        Assert.assertEquals(user.getCode(), object.getString("code"));
        Assert.assertEquals(converter.toString(user.getRegister()), object.getString("register"));
        Assert.assertEquals(user.getGrade(), object.getIntValue("grade"));
        Assert.assertEquals(user.getState(), object.getIntValue("state"));
    }

    protected void equalsSignUp(JSONObject data, String uid, int type, String password, String code) {
        AuthModel auth = liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{uid});
        Assert.assertEquals(type, auth.getType());
        Assert.assertEquals(auth.getUser(), data.getString("id"));
        for (String name : new String[]{"password", "name", "nick", "mobile", "email", "portrait", "address", "birthday"})
            Assert.assertFalse(data.containsKey(name));
        for (String name : new String[]{"gender", "grade", "state"})
            Assert.assertEquals(0, data.getIntValue(name));
        Assert.assertTrue(System.currentTimeMillis() - converter.toDate(data.getString("register")).getTime() < 2000L);
        if (code == null)
            Assert.assertEquals(8, data.getString("code").length());
        else
            Assert.assertEquals(code, data.getString("code"));

        UserModel user = liteOrm.findById(UserModel.class, auth.getUser());
        if (password == null)
            Assert.assertNull(user.getPassword());
        else
            Assert.assertEquals(digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME)), user.getPassword());
        Assert.assertNull(user.getName());
        Assert.assertNull(user.getNick());
        Assert.assertNull(user.getMobile());
        Assert.assertNull(user.getEmail());
        Assert.assertNull(user.getPortrait());
        Assert.assertNull(user.getAddress());
        Assert.assertNull(user.getBirthday());
        Assert.assertEquals(0, user.getGender());
        Assert.assertEquals(0, user.getGrade());
        Assert.assertEquals(0, user.getState());
        Assert.assertTrue(System.currentTimeMillis() - user.getRegister().getTime() < 2000L);
        if (code == null)
            Assert.assertEquals(8, user.getCode().length());
        else
            Assert.assertEquals(code, user.getCode());
    }
}
