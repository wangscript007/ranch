package org.lpw.ranch.user.auth;

import org.lpw.tephra.cache.Cache;
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
    protected AuthDao authDao;

    @Override
    public AuthModel findByUid(String uid) {
        String cacheKey = CACHE_UID + uid;
        AuthModel auth = cache.get(cacheKey);
        if (auth == null)
            cache.put(cacheKey, auth = authDao.findByUid(uid), false);

        return auth;
    }
}
