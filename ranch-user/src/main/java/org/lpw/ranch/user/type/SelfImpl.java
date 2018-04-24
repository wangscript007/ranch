package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.ranch.user.UserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service("ranch.user.type.self")
public class SelfImpl implements Type {
    @Inject
    private UserService userService;

    @Override
    public int getKey() {
        return Types.SELF;
    }

    @Override
    public String getUid(String uid, String password) {
        return uid;
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
        user.setPassword(userService.password(password));
    }

    @Override
    public String getNick(String uid, String password) {
        return null;
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        return null;
    }
}
