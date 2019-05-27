package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.user.types")
public class TypesImpl implements Types, ContextRefreshedListener {
    private Map<Integer, Type> map;

    @Override
    public String getUid(String uid, String password, int type) {
        return map.get(type).getUid(uid, password);
    }

    @Override
    public String getUid2(String uid, String password, int type) {
        return map.get(type).getUid2(uid, password);
    }

    @Override
    public void signUp(UserModel user, String uid, String password, int type) {
        map.get(type).signUp(user, uid, password);
    }

    @Override
    public String getNick(String uid, String password, int type) {
        return map.get(type).getNick(uid, password);
    }

    @Override
    public String getPortrait(String uid, String password, int type) {
        return map.get(type).getPortrait(uid, password);
    }

    @Override
    public JSONObject getAuth(String uid, String password, int type) {
        return map.get(type).getAuth(uid, password);
    }

    @Override
    public int getContextRefreshedSort() {
        return 15;
    }

    @Override
    public void onContextRefreshed() {
        map = new HashMap<>();
        BeanFactory.getBeans(Type.class).forEach(type -> map.put(type.getKey(), type));
    }
}
