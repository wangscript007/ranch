package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Autowired
    protected Generator generator;
    @Autowired
    protected Message message;
    @Autowired
    protected Converter converter;
    @Autowired
    protected Digest digest;
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected Request request;
    @Autowired
    protected MockHelper mockHelper;

    protected UserModel create(int i) {
        UserModel user = new UserModel();
        user.setPassword(digest.md5(UserModel.NAME + digest.sha1("password " + i + UserModel.NAME)));
        user.setName("name " + i);
        user.setNick("nick " + i);
        user.setMobile("mobile " + i);
        user.setEmail("email " + i);
        user.setPortrait("portrait " + i);
        user.setGender(i);
        user.setAddress("address " + i);
        user.setBirthday(new Date(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        user.setCode("code " + i);
        user.setRegister(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Hour.getTime()));
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
        Assert.assertFalse(object.has("password"));
        Assert.assertEquals(user.getName(), object.getString("name"));
        Assert.assertEquals(user.getNick(), object.getString("nick"));
        Assert.assertEquals(user.getMobile(), object.getString("mobile"));
        Assert.assertEquals(user.getEmail(), object.getString("email"));
        Assert.assertEquals(user.getPortrait(), object.getString("portrait"));
        Assert.assertEquals(user.getGender(), object.getInt("gender"));
        Assert.assertEquals(user.getAddress(), object.getString("address"));
        Assert.assertEquals(converter.toString(user.getBirthday()), object.getString("birthday"));
        Assert.assertEquals(user.getCode(), object.getString("code"));
        Assert.assertEquals(converter.toString(user.getRegister()), object.getString("register"));
    }
}
