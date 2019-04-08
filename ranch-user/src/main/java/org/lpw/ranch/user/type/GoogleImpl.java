package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.google.helper.GoogleHelper;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.util.Context;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.google")
public class GoogleImpl implements Type {
    @Inject
    private Context context;
    @Inject
    private Request request;
    @Inject
    private GoogleHelper googleHelper;

    @Override
    public int getKey() {
        return Types.GOOGLE;
    }

    @Override
    public String getUid(String uid, String password) {
        return get(uid, password, "id");
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
    }

    @Override
    public String getNick(String uid, String password) {
        return get(uid, password, "nick");
    }

    @Override
    public String getPortrait(String uid, String password) {
        return get(uid, password, "portrait");
    }

    private String get(String uid, String password, String name) {
        JSONObject object = getAuth(uid, password);

        return object == null || !object.containsKey(name) ? null : object.getString(name);
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.google:" + uid + "-" + password;
        JSONObject object = context.getThreadLocal(key);
        if (object == null)
            context.putThreadLocal(key, object = googleHelper.auth(password, uid));

        return object;
    }
}
