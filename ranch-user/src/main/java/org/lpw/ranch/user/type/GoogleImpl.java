package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.google.helper.GoogleHelper;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.google")
public class GoogleImpl extends TypeSupport {
    @Inject
    private Context context;
    @Inject
    private Validator validator;
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
        if (!validator.isEmail(user.getEmail()))
            user.setEmail(get(uid, password, "email"));
    }

    @Override
    public String getNick(String uid, String password) {
        return get(uid, password, "nick");
    }

    @Override
    public String getPortrait(String uid, String password) {
        return get(uid, password, "portrait");
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
