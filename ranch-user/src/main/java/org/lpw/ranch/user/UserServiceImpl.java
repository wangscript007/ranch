package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.auth.AuthService;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.weixin.WeixinService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(UserModel.NAME + ".service")
public class UserServiceImpl implements UserService {
    private static final String CACHE_MODEL = UserModel.NAME + ".service.model:";
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
    private WeixinService weixinService;
    @Inject
    private Pagination pagination;
    @Inject
    private AuthService authService;
    @Inject
    private UserDao userDao;

    @Override
    public void signUp(String uid, String password, Type type) {
        UserModel user = new UserModel();
        if (type == Type.Self)
            user.setPassword(password(password));
        user.setRegister(dateTime.now());
        while (user.getCode() == null) {
            String code = generator.random(8);
            if (userDao.findByCode(code) == null)
                user.setCode(code);
        }
        userDao.save(user);
        authService.create(user.getId(), uid, type.ordinal());
        session.set(SESSION, user);
    }

    @Override
    public boolean signIn(String uid, String password, String macId, Type type) {
        if (type == Type.WeiXin)
            uid = getWeixinOpenId(password, uid);
        if (uid == null)
            return false;

        AuthModel auth = authService.findByUid(uid);
        if (auth == null)
            return false;

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

    private String getWeixinOpenId(String appId, String code) {
        String openId = weixinService.auth(appId, code);
        if (openId == null)
            return null;

        if (authService.findByUid(openId) == null) {
            signUp(openId, null, Type.WeiXin);
            UserModel user = new UserModel();
            user.setNick(weixinService.getNickname());
            modify(user);
            portrait(weixinService.getPortrait());
        }

        return openId;
    }

    @Override
    public JSONObject sign() {
        UserModel user = session.get(SESSION);

        return user == null ? new JSONObject() : getJson(user.getId(), user);
    }

    @Override
    public void modify(UserModel user) {
        UserModel model = session.get(SESSION);
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
        if (user.getAddress() != null)
            model.setAddress(user.getAddress());
        if (user.getBirthday() != null)
            model.setBirthday(user.getBirthday());
        userDao.save(model);
        session.set(SESSION, model);
        cleanCache(model);
    }

    @Override
    public boolean password(String oldPassword, String newPassword) {
        UserModel user = session.get(SESSION);
        if (!validator.isEmpty(user.getPassword()) && !user.getPassword().equals(password(oldPassword)))
            return false;

        user.setPassword(password(newPassword));
        userDao.save(user);
        session.set(SESSION, user);
        cleanCache(user);

        return true;
    }

    @Override
    public void portrait(String uri) {
        UserModel user = session.get(SESSION);
        user.setPortrait(uri);
        userDao.save(user);
        cleanCache(user);
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
    public JSONObject find(String code) {
        String cacheKey = CACHE_JSON + code;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            UserModel user = userDao.findByCode(code);
            cache.put(cacheKey, object = user == null ? new JSONObject() : getJson(user.getId(), user), false);
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

    @Override
    public UserModel findById(String id) {
        String cacheKey = CACHE_MODEL + id;
        UserModel user = cache.get(cacheKey);
        if (user == null)
            cache.put(cacheKey, user = userDao.findById(id), false);

        return user;
    }

    private String password(String password) {
        return digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME));
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
        cleanCache(user);
    }

    @Override
    public void state(String id, int state) {
        UserModel user = findById(id);
        user.setState(state);
        userDao.save(user);
        cleanCache(user);
    }

    private void cleanCache(UserModel user) {
        cache.remove(CACHE_MODEL + user.getId());
        cache.remove(CACHE_JSON + user.getId());
        cache.remove(CACHE_JSON + user.getCode());
    }
}
