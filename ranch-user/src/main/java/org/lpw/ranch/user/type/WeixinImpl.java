package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.weixin")
public class WeixinImpl extends WeixinTypeSupport {
    @Inject
    private Context context;
    @Inject
    private WeixinHelper weixinHelper;

    @Override
    public int getKey() {
        return Types.WEIXIN;
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
        JSONObject object = getAuth(uid, password);
        if (validator.isEmpty(user.getNick()))
            user.setNick(object.getString("nickname"));
        if (validator.isEmpty(user.getPortrait()))
            user.setPortrait(object.getString("headimgurl"));
    }

    @Override
    public String getPortrait(String uid, String password) {
        return get(uid, password, "headimgurl");
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.weixin.uid-password:" + uid + "-" + password;
        JSONObject object = context.getThreadLocal(key);
        if (object == null)
            context.putThreadLocal(key, object = weixinHelper.auth(password, uid));

        return object;
    }
}
