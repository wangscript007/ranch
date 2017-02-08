package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.auth.AuthService;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(UserModel.NAME + ".service")
public class UserServiceImpl implements UserService {
    private static final String CACHE_JSON = UserModel.NAME + ".service.json:";
    private static final String SESSION = UserModel.NAME + ".service.session";

    @Inject
    private Cache cache;
    @Inject
    private Digest digest;
    @Inject
    private Converter converter;
    @Inject
    private Validator validator;
    @Inject
    private Generator generator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Session session;
    @Inject
    private AuthService authService;
    @Inject
    private UserDao userDao;

    @Override
    public boolean signUp(String uid, String password, int type) {
        if (authService.findByUid(uid) != null)
            return false;

        signUpDirect(uid, password, type);

        return true;
    }

    private void signUpDirect(String uid, String password, int type) {
        UserModel user = new UserModel();
        if (type == 1)
            user.setPassword(password(password));
        user.setRegister(dateTime.now());
        while (user.getCode() == null) {
            String code = generator.random(8);
            if (userDao.count(code) == 0)
                user.setCode(code);
        }
        userDao.save(user);
        authService.create(user.getId(), uid, type);
        session.set(SESSION, user);
    }

    @Override
    public boolean signIn(String uid, String password, String macId, int type) {
        AuthModel auth = authService.findByUid(uid);
        if (auth == null) {
            if (type < 2)
                return false;

            signUpDirect(uid, password, type);

            return true;
        }

        UserModel user = findById(auth.getUser());
        if (user == null || user.getState() != 0)
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

    @Override
    public JSONObject get(String[] ids) {
        JSONObject object = new JSONObject();
        for (String id : ids) {
            JSONObject user = getJson(id, null);
            if (user.isEmpty())
                continue;

            object.put(id, user);
        }

        return object;
    }

    private JSONObject getJson(String id, UserModel user) {
        String cacheKey = CACHE_JSON + id;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            if (user == null)
                user = findById(id);
            object = user == null ? new JSONObject() : modelHelper.toJson(user);
            cache.put(cacheKey, object, false);
        }

        return object;
    }


    private UserModel findById(String id) {
        return userDao.findById(id);
    }

    private String password(String password) {
        return digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME));
    }
}
