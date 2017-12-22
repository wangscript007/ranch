package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.online.OnlineModel;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.PageTester;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
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
    Generator generator;
    @Inject
    Message message;
    @Inject
    Converter converter;
    @Inject
    DateTime dateTime;
    @Inject
    Digest digest;
    @Inject
    Sign sign;
    @Inject
    LiteOrm liteOrm;
    @Inject
    Session session;
    @Inject
    MockHelper mockHelper;
    @Inject
    PageTester pageTester;

    UserModel create(int i) {
        return create(i, 10 + i);
    }

    UserModel create(int i, int state) {
        return set(new UserModel(), i, state);
    }

    UserModel set(UserModel user, int i, int state) {
        user.setPassword(digest.md5(UserModel.NAME + digest.sha1("password " + i + UserModel.NAME)));
        user.setSecret(digest.md5(UserModel.NAME + digest.sha1("secret " + i + UserModel.NAME)));
        user.setIdcard("idcard " + i);
        user.setName("name " + i);
        user.setNick("nick " + i);
        user.setMobile("123123456" + (10 + i));
        user.setEmail("email " + i);
        user.setPortrait("portrait " + i);
        user.setGender(i);
        user.setBirthday(new Date(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        user.setCode("code " + i);
        user.setRegister(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Hour.getTime()));
        user.setGrade(i);
        user.setState(state);
        liteOrm.save(user);

        return user;
    }

    AuthModel createAuth(String user, String uid, int type) {
        AuthModel auth = new AuthModel();
        auth.setUser(user);
        auth.setUid(uid);
        auth.setType(type);
        liteOrm.save(auth);

        return auth;
    }

    void online(UserModel user) {
        OnlineModel online = liteOrm.findOne(new LiteQuery(OnlineModel.class).where("c_sid=?"), new Object[]{session.getId()});
        if (online == null)
            online = new OnlineModel();
        online.setUser(user.getId());
        online.setIp("ip");
        online.setSid(session.getId());
        online.setSignIn(dateTime.now());
        online.setLastVisit(dateTime.now());
        liteOrm.save(online);
    }

    void equals(UserModel user, JSONObject object) {
        Assert.assertEquals(user.getId(), object.getString("id"));
        Assert.assertFalse(object.containsKey("password"));
        Assert.assertFalse(object.containsKey("secret"));
        Assert.assertEquals(user.getIdcard(), object.getString("idcard"));
        Assert.assertEquals(user.getName(), object.getString("name"));
        Assert.assertEquals(user.getNick(), object.getString("nick"));
        Assert.assertEquals(user.getMobile(), object.getString("mobile"));
        Assert.assertEquals(user.getEmail(), object.getString("email"));
        Assert.assertEquals(user.getPortrait(), object.getString("portrait"));
        Assert.assertEquals(user.getGender(), object.getIntValue("gender"));
        Assert.assertEquals(converter.toString(user.getBirthday()), object.getString("birthday"));
        Assert.assertEquals(user.getCode(), object.getString("code"));
        Assert.assertEquals(converter.toString(user.getRegister()), object.getString("register"));
        Assert.assertEquals(user.getGrade(), object.getIntValue("grade"));
        Assert.assertEquals(user.getState(), object.getIntValue("state"));
    }

    void auth(String user, String uid, int type, long[] times) {
        AuthModel auth = find(uid);
        Assert.assertEquals(user, auth.getUser());
        Assert.assertEquals(type, auth.getType());
        Assert.assertTrue(System.currentTimeMillis() - auth.getTime().getTime() > times[0]);
        Assert.assertTrue(System.currentTimeMillis() - auth.getTime().getTime() < times[1]);
    }

    AuthModel find(String uid) {
        return liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{uid});
    }

    void equalsSignUp(JSONObject data, String uid, int type, String password, String code) {
        equalsSignUp(data, uid, type, password, System.currentTimeMillis(), code);
    }

    void equalsSignUp(JSONObject data, String uid, int type, String password, long register, String code) {
        AuthModel auth = liteOrm.findOne(new LiteQuery(AuthModel.class).where("c_uid=?"), new Object[]{uid});
        Assert.assertEquals(type, auth.getType());
        Assert.assertEquals(auth.getUser(), data.getString("id"));
        for (String name : new String[]{"password", "name", "nick", "mobile", "email", "portrait", "address", "birthday"})
            Assert.assertFalse(data.containsKey(name));
        for (String name : new String[]{"gender", "grade", "state"})
            Assert.assertEquals(0, data.getIntValue(name));
        Assert.assertTrue(Math.abs(register - dateTime.toDate(data.getString("register")).getTime()) < 2000L);
        if (code == null)
            Assert.assertEquals(8, data.getString("code").length());
        else
            Assert.assertEquals(code, data.getString("code"));

        UserModel user = liteOrm.findById(UserModel.class, auth.getUser());
        if (password == null)
            Assert.assertNull(user.getPassword());
        else
            Assert.assertEquals(digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME)), user.getPassword());
        Assert.assertNull(user.getSecret());
        Assert.assertNull(user.getIdcard());
        Assert.assertNull(user.getName());
        Assert.assertNull(user.getNick());
        Assert.assertNull(user.getMobile());
        Assert.assertNull(user.getEmail());
        Assert.assertNull(user.getPortrait());
        Assert.assertNull(user.getBirthday());
        Assert.assertEquals(0, user.getGender());
        Assert.assertEquals(0, user.getGrade());
        Assert.assertEquals(0, user.getState());
        Assert.assertTrue(Math.abs(register - user.getRegister().getTime()) < 2000L);
        if (code == null)
            Assert.assertEquals(8, user.getCode().length());
        else
            Assert.assertEquals(code, user.getCode());
    }
}
