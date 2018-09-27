package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.auth.AuthService;
import org.lpw.ranch.user.online.OnlineModel;
import org.lpw.ranch.user.online.OnlineService;
import org.lpw.ranch.user.type.Types;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.helper.WeixinHelper;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.TimeUnit;
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
    private static final String SESSION_INSTRODUCER = UserModel.NAME + ".service.session.inviter";
    private static final String SESSION_AUTH3 = UserModel.NAME + ".service.session.auth3";
    private static final String SESSION_UID = UserModel.NAME + ".service.session.uid";

    @Inject
    private Cache cache;
    @Inject
    private Digest digest;
    @Inject
    private Converter converter;
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
    private Types types;
    @Inject
    private AuthService authService;
    @Inject
    private OnlineService onlineService;
    @Inject
    private UserDao userDao;
    @Value("${tephra.ctrl.service-root:}")
    private String root;
    private int codeLength = 8;

    @Override
    public String inviter(String code) {
        if (!validator.isEmpty(code))
            session.set(SESSION_INSTRODUCER, code);

        return session.get(SESSION_INSTRODUCER);
    }

    @Override
    public void signUp(String uid, String password, int type) {
        UserModel user = fromSession();
        if (user == null)
            user = new UserModel();
        types.signUp(user, uid, password, type);
        if (user.getRegister() == null)
            user.setRegister(dateTime.now());
        for (int i = 0; i < 1024 && user.getCode() == null; i++) {
            String code = generator.random(codeLength);
            if (userDao.findByCode(code) == null)
                user.setCode(code);
        }
        if (type == 1) {
            if (validator.isMobile(uid))
                user.setMobile(uid);
            else if (validator.isEmail(uid))
                user.setEmail(uid);
        }
        setInviter(user);
        if (validator.isEmpty(user.getPortrait()))
            user.setPortrait(types.getPortrait(uid, password, type));
        String nick = types.getNick(uid, password, type);
        if (validator.isEmpty(user.getNick()))
            user.setNick(validator.isEmpty(nick) ? uid : nick);
        userDao.save(user);
        authService.create(user.getId(), types.getUid(uid, password, type), type, nick, types.getPortrait(uid, password, type));
        clearCache(user);
        onlineService.signIn(user.getId());
        session.set(SESSION, user);
    }

    private void setInviter(UserModel user) {
        if (!validator.isEmpty(user.getInviter()))
            return;

        String code = inviter(null);
        if (validator.isEmpty(code) || code.length() != codeLength)
            return;

        UserModel inviter = userDao.findByCode(code.toLowerCase());
        if (inviter == null)
            return;

        user.setInviter(inviter.getId());
        inviter.setInviteCount(inviter.getInviteCount() + 1);
        userDao.save(inviter);
        clearCache(inviter);
    }

    @Override
    public boolean signIn(String uid, String password, int type) {
        if (type == Types.WEIXIN || type == Types.WEIXIN_MINI)
            uid = getWeixinId(uid, password, type);
        if (uid == null)
            return false;

        AuthModel auth = authService.findByUid(uid);
        if (auth == null || !sameType(auth, type))
            return false;

        UserModel user = findById(auth.getUser());
        if (user == null || user.getState() != 0)
            return false;

        if (type == Types.SELF) {
            if (!pass(user, password))
                return false;
        } else if (type > Types.SELF)
            session.set(SESSION_AUTH3, types.getAuth(uid, password, type));
        onlineService.signIn(user.getId());
        session.set(SESSION, user);
        session.set(SESSION_UID, uid);

        return true;
    }

    private String getWeixinId(String uid, String password, int type) {
        String wxid = types.getUid(uid, password, type);
        if (wxid == null)
            return null;

        if (authService.findByUid(wxid) == null)
            signUp(uid, password, type);

        return wxid;
    }

    private boolean sameType(AuthModel auth, int type) {
        return auth.getType() == type || (isWeixinType(auth.getType()) && isWeixinType(type));
    }

    private boolean isWeixinType(int type) {
        return type == Types.WEIXIN || type == Types.WEIXIN_MINI;
    }

    private boolean pass(UserModel user, String password) {
        if (validator.isEmpty(password))
            return false;

        String cacheKey = CACHE_PASS + user.getId();
        String[] failures = converter.toArray(cache.get(cacheKey), ",");
        int failure = failures.length < 2 ? 0 : numeric.toInt(failures[0]);
        if (failure > 0 && System.currentTimeMillis() - numeric.toLong(failures[1]) >
                classifyHelper.valueAsInt(UserModel.NAME + ".pass", "lock", 5) * TimeUnit.Minute.getTime()) {
            failure = 0;
            cache.remove(cacheKey);
        }
        int max = failure > 0 ? classifyHelper.valueAsInt(UserModel.NAME + ".pass", "max-failure", 5) : 0;
        if (failure <= max && user.getPassword().equals(password(password))) {
            cache.remove(cacheKey);

            return true;
        }

        cache.put(cacheKey, failure + 1 + "," + System.currentTimeMillis(), false);

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
        boolean success = !validator.isEmpty(key) && !validator.isEmpty(code) && signIn(code, key, Types.WEIXIN);

        return redirectUrl + (redirectUrl.indexOf('?') == -1 ? "?" : "&") + "state=" + (success ? "success" : "failure");
    }

    @Override
    public JSONObject sign() {
        if (!onlineService.isSign())
            return new JSONObject();

        UserModel user = fromSession();
        JSONObject object = getJson(user.getId(), user);
        JSONObject auth3 = session.get(SESSION_AUTH3);
        if (auth3 != null)
            object.put("auth3", auth3);

        return object;
    }

    @Override
    public void signOut() {
        onlineService.signOut();
        session.remove(SESSION);
        session.remove(SESSION_AUTH3);
        session.remove(SESSION_UID);
    }

    @Override
    public void signOut(String sid) {
        session.remove(sid, SESSION);
        session.remove(sid, SESSION_AUTH3);
        session.remove(sid, SESSION_UID);
    }

    @Override
    public void modify(UserModel user) {
        session.set(SESSION, save(fromSession().getId(), user));
    }

    @Override
    public boolean password(String oldPassword, String newPassword) {
        UserModel user = fromSession();
        if (!validator.isEmpty(user.getPassword()) && !user.getPassword().equals(password(oldPassword)))
            return false;

        user.setPassword(password(newPassword));
        save(user);
        session.set(SESSION, user);

        return true;
    }

    @Override
    public String password(String password) {
        return digest.md5(UserModel.NAME + digest.sha1(password + UserModel.NAME));
    }

    @Override
    public UserModel fromSession() {
        UserModel user = session.get(SESSION);
        if (user == null) {
            OnlineModel online = onlineService.findBySid(session.getId());
            if (online != null)
                session.set(SESSION, user = findById(online.getUser()));
        }

        return user;
    }

    @Override
    public String uidFromSession() {
        return session.get(SESSION_UID);
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
    public JSONObject get(String id) {
        return getJson(id, null);
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
    public JSONObject query(String uid, String idcard, String name, String nick, String mobile, String email, String code,
                            int minGrade, int maxGrade, int state, String[] register) {
        if (validator.isEmpty(uid))
            return userDao.query(idcard, name, nick, mobile, email, code, minGrade, maxGrade, state,
                    register.length > 0 ? dateTime.getStart(register[0]) : null, register.length > 1 ? dateTime.getEnd(register[1]) : null,
                    pagination.getPageSize(20), pagination.getPageNum()).toJson();

        AuthModel auth = authService.findByUid(uid);
        if (auth == null)
            return BeanFactory.getBean(PageList.class).setPage(0, pagination.getPageSize(20), 0).toJson();

        JSONObject object = BeanFactory.getBean(PageList.class).setPage(1, pagination.getPageSize(20), 1).toJson();
        object.getJSONArray("list").add(getJson(auth.getUser(), null));

        return object;
    }

    @Override
    public void update(UserModel user) {
        save(user.getId(), user);
    }

    private UserModel save(String id, UserModel user) {
        UserModel model = findById(id);
        model.setIdcard(user.getIdcard());
        model.setName(user.getName());
        model.setNick(user.getNick());
        model.setMobile(user.getMobile());
        model.setEmail(user.getEmail());
        model.setPortrait(user.getPortrait());
        model.setGender(user.getGender());
        model.setBirthday(user.getBirthday());
        save(model);

        return model;
    }

    @Override
    public void grade(String id, int grade) {
        UserModel user = findById(id);
        user.setGrade(grade);
        save(user);
    }

    @Override
    public void state(String id, int state) {
        UserModel user = findById(id);
        user.setState(state);
        save(user);
    }

    private void save(UserModel user) {
        userDao.save(user);
        clearCache(user);
    }

    @Override
    public void clearCache() {
        clearCache(fromSession());
    }

    private void clearCache(UserModel user) {
        cache.remove(CACHE_MODEL + user.getId());
        cache.remove(CACHE_JSON + user.getId());
        cache.remove(CACHE_JSON + user.getCode());
    }
}
