package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.weixin.mini")
public class WeixinMiniImpl implements Type {
    @Inject
    private Validator validator;
    @Inject
    private Context context;
    @Inject
    private Request request;
    @Inject
    private WeixinHelper weixinHelper;

    @Override
    public int getKey() {
        return Types.WEIXIN_MINI;
    }

    @Override
    public String getUid(String uid, String password) {
        return weixinHelper.getId(getAuth(uid, password));
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
    }

    @Override
    public String getNick(String uid, String password) {
        return getAuth(uid, password).getString("nickName");
    }

    @Override
    public String getPortrait(String uid, String password) {
        return getAuth(uid, password).getString("avatarUrl");
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.weixin-mini.uid-password:" + uid + "-" + password;
        JSONObject object = context.getThreadLocal(key);
        if (object == null)
            context.putThreadLocal(key, object = weixinHelper.auth(password, uid, request.get("iv"), request.get("message"),
                    request.get("iv2"), request.get("message2")));

        return object;
    }
}
