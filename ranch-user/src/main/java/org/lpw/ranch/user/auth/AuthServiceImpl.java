package org.lpw.ranch.user.auth;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.user.type.Types;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AuthModel.NAME + ".service")
public class AuthServiceImpl implements AuthService {
    private static final String CACHE_UID = AuthModel.NAME + ".service.uid:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private AuthDao authDao;
    @Value("${" + AuthModel.NAME + ".bind.effective:604800}")
    private long effective;

    @Override
    public JSONArray query(String user) {
        return modelHelper.toJson(authDao.query(user).getList());
    }

    @Override
    public AuthModel create(String userId, String uid, int type) {
        AuthModel auth = new AuthModel();
        auth.setUser(userId);
        auth.setUid(uid);
        auth.setType(type);
        authDao.save(auth);

        return auth;
    }

    @Override
    public AuthModel findByUid(String uid) {
        String cacheKey = CACHE_UID + uid;
        AuthModel auth = cache.get(cacheKey);
        if (auth == null) {
            auth = authDao.findByUid(uid);
            if (auth == null || (auth.getType() == Types.BIND && auth.getTime() != null
                    && System.currentTimeMillis() - auth.getTime().getTime() > effective * 1000))
                return null;

            cache.put(cacheKey, auth, false);
        }

        return auth;
    }

    @Override
    public void bind(String user, String id) {
        if (validator.isEmpty(id))
            return;

        AuthModel auth = findByUid(id);
        if (auth == null) {
            auth = new AuthModel();
            auth.setUid(id);
        } else if (auth.getUser().equals(user))
            return;

        auth.setUser(user);
        auth.setTime(dateTime.now());
        auth.setType(Types.BIND);
        authDao.save(auth);
    }

    @Override
    public void unbind(String id) {
        AuthModel auth = findByUid(id);
        if (auth == null || auth.getType() != Types.BIND)
            return;

        authDao.delete(auth);
        cache.remove(CACHE_UID + id);
    }
}
