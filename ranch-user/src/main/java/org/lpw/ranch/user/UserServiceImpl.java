package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.auth.AuthService;
import org.lpw.ranch.user.online.OnlineService;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(UserModel.NAME + ".service")
public class UserServiceImpl implements UserService {
    private static final String CACHE_MODEL = UserModel.NAME + ".service.model:";
    private static final String CACHE_JSON = UserModel.NAME + ".service.json:";
    private static final String CACHE_PASS = UserModel.NAME + ".service.pass:";
    private static final String SESSION = UserModel.NAME + ".service.session";

    @Inject
    private Cache cache;
    @Inject
    private Digest digest;
    @Inject
    private Numeric numeric;
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
    private Carousel carousel;
    @Inject
    private Pagination pagination;
    @Inject
    private ClassifyHelper classifyHelper;
    @Inject
    private WeixinHelper weixinHelper;
    @Inject
    private AuthService authService;
    @Inject
    private OnlineService onlineService;
    @Inject
    private UserDao userDao;
    @Value("${tephra.ctrl.service-root:}")
    private String root;

    @Override
    public void signUp(String uid, String password, Type type) {
        UserModel user = session.get(SESSION);
        if (user == null)
            user = new UserModel();
        if (type == Type.Self)
            user.setPassword(password(password));
        if (user.getRegister() == null)
            user.setRegister(dateTime.now());
        while (user.getCode() == null) {
            String code = generator.random(8);
            if (userDao.findByCode(code) == null)
                user.setCode(code);
        }
        if (validator.isMobile(uid))
            user.setMobile(uid);
        else if (validator.isEmail(uid))
            user.setEmail(uid);
        userDao.save(user);
        authService.create(user.getId(), uid, type.ordinal());
        clearCache(user);
        onlineService.signIn(user.getId());
        session.set(SESSION, user);
    }

    @Override
    public boolean signIn(String uid, String password, String macId, Type type) {
        if (type == Type.WeiXin)
            uid = getWeixinId(password, uid);
        if (uid == null)
            return false;

        AuthModel auth = type == Type.Bind ? findByBind(uid) : authService.findByUid(uid);
        if (auth == null || auth.getType() != type.ordinal())
            return false;

        UserModel user = findById(auth.getUser());
        if (user == null || user.getState() != 0)
            return false;

        if (auth.getType() == 1) {
            if (!pass(user, password))
                return false;

            authService.bind(user.getId(), macId);
            authService.bind(user.getId(), session.getId());
        }
        onlineService.signIn(user.getId());
        session.set(SESSION, user);

        return true;
    }

    private AuthModel findByBind(String uid) {
        AuthModel auth = authService.findByUid(uid);
        if (auth != null)
            return auth;

        signUp(uid, null, Type.Bind);

        return authService.findByUid(uid);
    }

    private String getWeixinId(String key, String code) {
        JSONObject object = weixinHelper.auth(key, code);
        String uid = weixinHelper.getId(object);
        if (uid == null)
            return null;

        if (authService.findByUid(uid) == null) {
            signUp(uid, null, Type.WeiXin);
            UserModel user = new UserModel();
            user.setNick(object.getString("nickname"));
            modify(user);
            portrait(object.getString("headimgurl"));
        }

        return uid;
    }

    private boolean pass(UserModel user, String password) {
        if (validator.isEmpty(password))
            return false;

        String key = CACHE_PASS + user.getId();
        int failure = numeric.toInt(cache.get(key));
        int max = failure > 0 ? numeric.toInt(classifyHelper.value(UserModel.NAME + ".pass", "max-failure")) : 0;
        if (max <= 0)
            max = 5;
        if (failure <= max && user.getPassword().equals(password(password))) {
            cache.remove(key);

            return true;
        }

        cache.put(key, failure + 1, false);

        return false;
    }

    @Override
    public String signInWxPc(String key, String redirectUrl) {
        session.set("sign-in-wx-pc-key", key);
        session.set("sign-in-wx-pc-redirect-url", redirectUrl);

        return weixinHelper.getPcSignInUrl(key, root + "/user/sign-in-wx-pc-redirect?tephra-session-id=" + session.getId());
    }

    @Override
    public String signInWxPcRedirect(String code) {
        String key = session.get("sign-in-wx-pc-key");
        String redirectUrl = session.get("sign-in-wx-pc-redirect-url");
        boolean success = !validator.isEmpty(key) && !validator.isEmpty(code) && signIn(code, key, null, Type.WeiXin);

        return redirectUrl + (redirectUrl.indexOf('?') == -1 ? "?" : "&") + "state=" + (success ? "success" : "failure");
    }

    @Override
    public JSONObject sign() {
        if (!onlineService.isSign())
            return new JSONObject();

        UserModel user = session.get(SESSION);
        if (user == null) {
            AuthModel auth = authService.findByUid(session.getId());
            if (auth != null && auth.getType() == Type.Bind.ordinal()) {
                user = findById(auth.getUser());
                if (user != null) {
                    authService.bind(user.getId(), session.getId());
                    session.set(SESSION, user = findById(auth.getUser()));
                }
            }
        }

        return user == null ? new JSONObject() : getJson(user.getId(), user);
    }

    @Override
    public void signOut() {
        authService.unbind(session.getId());
        onlineService.signOut();
        session.remove(SESSION);
    }

    @Override
    public void signOut(String sid) {
        authService.unbind(sid);
        session.remove(sid, SESSION);
    }

    @Override
    public void modify(UserModel user) {
        UserModel model = session.get(SESSION);
        if (user.getIdcard() != null)
            model.setIdcard(user.getIdcard());
        if (user.getName() != null)
            model.setName(user.getName());
        if (user.getNick() != null)
            model.setNick(user.getNick());
        if (user.getMobile() != null)
            model.setMobile(user.getMobile());
        if (user.getEmail() != null)
            model.setEmail(user.getEmail());
        if (user.getGender() > 0)
            model.setGender(user.getGender());
        if (user.getBirthday() != null)
            model.setBirthday(user.getBirthday());
        userDao.save(model);
        session.set(SESSION, model);
        clearCache(model);
    }

    @Override
    public boolean password(String oldPassword, String newPassword) {
        UserModel user = session.get(SESSION);
        if (!validator.isEmpty(user.getPassword()) && !user.getPassword().equals(password(oldPassword)))
            return false;

        user.setPassword(password(newPassword));
        userDao.save(user);
        session.set(SESSION, user);
        clearCache(user);

        return true;
    }

    private String password(String password) {
        return digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME));
    }

    @Override
    public void portrait(String uri) {
        UserModel user = session.get(SESSION);
        user.setPortrait(uri);
        userDao.save(user);
        clearCache(user);
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

    @Override
    public UserModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        UserModel user = cache.get(cacheKey);
        if (user == null)
            cache.put(cacheKey, user = userDao.findById(id), false);

        return user;
    }

    @Override
    public JSONObject findByCode(String code) {
        String cacheKey = CACHE_JSON + code;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            UserModel user = userDao.findByCode(code);
            cache.put(cacheKey, object = user == null ? new JSONObject() : getJson(user.getId(), user), false);
        }

        return object;
    }

    @Override
    public JSONObject findByUid(String uid) {
        String cacheKey = CACHE_JSON + uid;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            UserModel user = findById(authService.findByUid(uid).getUser());
            cache.put(cacheKey, object = user == null ? new JSONObject() : getJson(user.getId(), user), false);
        }

        return object;
    }

    @Override
    public JSONObject findOrSign(String idUidCode) {
        UserModel user = findById(idUidCode);
        if (user == null) {
            AuthModel auth = authService.findByUid(idUidCode);
            if (auth != null)
                user = findById(auth.getUser());
        }
        if (user == null)
            user = userDao.findByCode(idUidCode);

        return user == null ? sign() : modelHelper.toJson(user);
    }

    private JSONObject getJson(String id, UserModel user) {
        String cacheKey = CACHE_JSON + id;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            if (user == null)
                user = findById(id);
            if (user == null)
                object = new JSONObject();
            else {
                object = modelHelper.toJson(user);
                object.put("auth", authService.query(user.getId()));
            }
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    @Override
    public JSONObject query(String mobile) {
        if (validator.isEmpty(mobile))
            return userDao.query(pagination.getPageSize(), pagination.getPageNum()).toJson();

        return userDao.query(mobile).toJson();
    }

    @Override
    public void grade(String id, int grade) {
        UserModel user = findById(id);
        user.setGrade(grade);
        userDao.save(user);
        clearCache(user);
    }

    @Override
    public void state(String id, int state) {
        UserModel user = findById(id);
        user.setState(state);
        userDao.save(user);
        clearCache(user);
    }

    private void clearCache(UserModel user) {
        cache.remove(CACHE_MODEL + user.getId());
        cache.remove(CACHE_JSON + user.getId());
        cache.remove(CACHE_JSON + user.getCode());
    }
}
