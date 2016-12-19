package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.auth.AuthService;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(UserModel.NAME + ".service")
public class UserServiceImpl implements UserService {
    private static final String CACHE_JSON = UserModel.NAME + ".service.json:";
    private static final String SESSION = UserModel.NAME + ".service.session";

    @Autowired
    protected Cache cache;
    @Autowired
    protected Digest digest;
    @Autowired
    protected Converter converter;
    @Autowired
    protected Validator validator;
    @Autowired
    protected ModelHelper modelHelper;
    @Autowired
    protected Session session;
    @Autowired
    protected AuthService authService;
    @Autowired
    protected UserDao userDao;

    @Override
    public boolean signIn(String uid, String password, String macId) {
        AuthModel auth = authService.findByUid(uid);
        if (auth == null)
            return false;

        UserModel user = findById(auth.getUser());
        if (user == null)
            return false;

        if (auth.getType() == 1) {
            if (validator.isEmpty(password) || !user.getPassword().equals(password(password)))
                return false;

            authService.bindMacId(user.getId(), macId);
        }

        session.set(SESSION, user);

        return true;
    }

    @Override
    public JSONObject sign() {
        UserModel user = session.get(SESSION);

        return user == null ? new JSONObject() : getJson(user.getId(), user);
    }

    protected JSONObject getJson(String id, UserModel user) {
        String cacheKey = CACHE_JSON + id;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            if (user == null)
                user = findById(id);
            if (user == null)
                object = new JSONObject();
            else
                object = modelHelper.toJson(user, converter.toSet(new String[]{"password"}));
            cache.put(cacheKey, object, false);
        }

        return object;
    }


    protected UserModel findById(String id) {
        return userDao.findById(id);
    }

    protected String password(String password) {
        return digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME));
    }
}
