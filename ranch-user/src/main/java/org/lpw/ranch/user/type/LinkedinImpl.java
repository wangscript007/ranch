package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.linkedin.helper.LinkedinHelper;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.util.Context;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.linkedin")
public class LinkedinImpl extends TypeSupport {
    @Inject
    private Context context;
    @Inject
    private Request request;
    @Inject
    private LinkedinHelper linkedinHelper;

    @Override
    public int getKey() {
        return Types.LINKEDIN;
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
        return get(uid, password, "formattedName");
    }

    @Override
    public String getPortrait(String uid, String password) {
        return get(uid, password, "pictureUrl");
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.linkedin:" + uid + "-" + password;
        JSONObject object = context.getThreadLocal(key);
        if (object == null)
            context.putThreadLocal(key, object = linkedinHelper.auth(password, uid, request.get("redirectUri")));

        return object;
    }
}
