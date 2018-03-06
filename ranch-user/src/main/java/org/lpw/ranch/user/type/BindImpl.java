package org.lpw.ranch.user.type;

import org.lpw.ranch.user.UserModel;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service("ranch.user.type.bind")
public class BindImpl implements Type {
    @Override
    public int getKey() {
        return Types.BIND;
    }

    @Override
    public String getUid(String uid, String password) {
        return uid;
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
    }
}
