package org.lpw.ranch.user.auth;

import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(AuthModel.NAME + ".service")
public class AuthServiceImpl implements AuthService {
    private static final String CACHE_UID = AuthModel.NAME + ".service.uid:";

    @Autowired
    protected Cache cache;
    @Autowired
    protected Validator validator;
    @Autowired
    protected AuthDao authDao;

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
