package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.cache.Cache;
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
    private Cache cache;
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
        return null;
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String cacheKey = "ranch.user.type.weixin-mini.uid-password:" + uid + "-" + password;
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, object = weixinHelper.auth(password, uid, 1), false);

        return object;
    }
}
