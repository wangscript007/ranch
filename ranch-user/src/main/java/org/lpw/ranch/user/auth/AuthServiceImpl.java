package org.lpw.ranch.user.auth;

import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.Validator;
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
    private AuthDao authDao;

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
        if (auth == null)
            cache.put(cacheKey, auth = authDao.findByUid(uid), false);

        return auth;
    }

    @Override
    public void bindMacId(String userId, String macId) {
        if (validator.isEmpty(macId))
            return;

        AuthModel auth = findByUid(macId);
        if (auth == null) {
            auth = new AuthModel();
            auth.setUid(macId);
        } else if (auth.getUser().equals(userId))
            return;

        auth.setUser(userId);
        authDao.save(auth);
    }
}
