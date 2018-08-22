package org.lpw.ranch.user.auth;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.user.UserService;
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
    private UserService userService;
    @Inject
    private AuthDao authDao;
    @Value("${" + AuthModel.NAME + ".bind.effective:604800}")
    private long effective;

    @Override
    public JSONArray query(String user) {
        return modelHelper.toJson(authDao.query(user).getList());
    }

    @Override
    public AuthModel create(String userId, String uid, int type, String nick, String portrait) {
        AuthModel auth = new AuthModel();
        auth.setUser(userId);
        auth.setUid(uid);
        auth.setTime(dateTime.now());
        auth.setType(type);
        auth.setNick(nick);
        auth.setPortrait(portrait);
        authDao.save(auth);

        return auth;
    }

    @Override
    public AuthModel findByUid(String uid) {
        String cacheKey = CACHE_UID + uid;
        AuthModel auth = cache.get(cacheKey);
        if (auth == null) {
            auth = authDao.findByUid(uid);
            if (auth != null)
                cache.put(cacheKey, auth, false);
        }

        return auth;
    }

    @Override
    public void delete() {
        AuthModel auth = findByUid(userService.uidFromSession());
        authDao.delete(auth);
        cache.remove(CACHE_UID + auth.getUid());
        userService.clearCache();
        userService.signOut();
    }
}
