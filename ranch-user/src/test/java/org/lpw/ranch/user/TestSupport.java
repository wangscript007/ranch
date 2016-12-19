package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Autowired
    protected Generator generator;
    @Autowired
    protected Message message;
    @Autowired
    protected Digest digest;
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected MockHelper mockHelper;

    protected UserModel create(int i) {
        UserModel user = new UserModel();
        user.setPassword(digest.md5(UserModel.NAME + digest.sha1("password " + i + UserModel.NAME)));
        user.setMobile("mobile " + i);
        user.setEmail("email " + i);
        user.setCode(generator.random(8));
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
        Assert.assertEquals(user.getMobile(), object.getString("mobile"));
        Assert.assertEquals(user.getEmail(), object.getString("email"));
        Assert.assertEquals(user.getCode(), object.getString("code"));
    }
}
