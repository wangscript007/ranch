package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.facebook.helper.FacebookHelper;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.util.Context;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.facebook")
public class FacebookImpl implements Type {
    @Inject
    private Context context;
    @Inject
    private FacebookHelper facebookHelper;

    @Override
    public int getKey() {
        return Types.FACEBOOK;
    }

    @Override
    public String getUid(String uid, String password) {
        JSONObject object = getAuth(uid, password);

        return object == null || !object.containsKey("id") ? null : object.getString("id");
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
    }

    @Override
    public String getNick(String uid, String password) {
        return null;
    }

    @Override
    public String getPortrait(String uid, String password) {
        return null;
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.facebook:" + uid + "-" + password;
        JSONObject object = context.getThreadLocal(key);
        if (object == null)
            context.putThreadLocal(key, object = facebookHelper.auth(password, uid));

        return object;
    }
}
