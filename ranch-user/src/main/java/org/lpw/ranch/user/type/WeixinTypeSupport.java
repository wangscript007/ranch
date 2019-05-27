package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.util.Validator;

import javax.inject.Inject;

/**
 * @author lpw
 */
abstract class WeixinTypeSupport extends TypeSupport{
    @Inject protected Validator validator;

    @Override
    public String getUid(String uid, String password) {
        return get(uid, password,"unionid");
    }

    @Override
    public String getUid2(String uid, String password) {
        return get(uid, password,"openid");
    }

    @Override
    public String getNick(String uid, String password) {
        return get(uid, password, "nickname");
    }
}
