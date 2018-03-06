package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.cache.Cache;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.weixin")
public class WeixinImpl implements Type {
    @Inject
    private Cache cache;
    @Inject
    private WeixinHelper weixinHelper;

    @Override
    public int getKey() {
        return Types.WEIXIN;
    }

    @Override
    public String getUid(String uid, String password) {
        return weixinHelper.getId(getAuth(uid, password));
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
        JSONObject object = getAuth(uid, password);
        user.setNick(object.getString("nickname"));
        user.setPortrait(object.getString("headimgurl"));
    }

    private JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.weixin.uid-password:" + uid + "-" + password;
        JSONObject object = cache.get(key);
        if (object == null)
            cache.put(key, object = weixinHelper.auth(password, uid), false);

        return object;
    }
}
