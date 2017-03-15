package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Comparator;

/**
 * @author lpw
 */
@Service(FriendModel.NAME + ".service")
public class FriendServiceImpl implements FriendService {
    private static final String CACHE_OWNER = FriendModel.NAME + ".service.owner:";
    private static final String CACHE_OWNER_USER = FriendModel.NAME + ".service.owner-user:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private FriendDao friendDao;

    @Override
    public JSONArray query(int state) {
        String owner = userHelper.id();
        String cacheKey = CACHE_OWNER + owner + state;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = modelHelper.toJson(friendDao.query(owner, state, state != 2).getList());
            array = userHelper.fill(array, new String[]{"user"});
            if (state == 2)
                array.sort(Comparator.comparing(this::getCompareName));
            cache.put(cacheKey, array, false);
        }

        return array;
    }

    private String getCompareName(Object object) {
        JSONObject json = (JSONObject) object;
        String memo = json.getString("memo");
        String name = validator.isEmpty(memo) ? json.getJSONObject("user").getString("nick") : memo;

        return validator.isEmpty(name) ? "" : name;
    }

    @Override
    public void create(String user, String memo) {
        String owner = userHelper.id();
        FriendModel model = find(owner, user);
        if (model == null) {
            create(owner, user, null, 0);
            create(user, owner, memo, 1);
        } else if (model.getState() == 1) {
            pass(owner, user, null, 1);
            pass(user, owner, memo, 0);
        }
    }

    private void create(String owner, String user, String memo, int state) {
        FriendModel model = new FriendModel();
        model.setOwner(owner);
        model.setUser(user);
        model.setMemo(memo);
        model.setState(state);
        model.setCreate(dateTime.now());
        friendDao.save(model);
        cleanCache(owner, user);
    }

    @Override
    public void pass(String user, String memo) {
        String owner = userHelper.id();
        pass(owner, user, memo, 1);
        pass(user, owner, null, 0);
    }

    private void pass(String owner, String user, String memo, int state) {
        FriendModel model = find(owner, user);
        if (model == null || model.getState() != state)
            return;

        if (!validator.isEmpty(memo))
            model.setMemo(memo);
        model.setState(2);
        friendDao.save(model);
        cleanCache(owner, user);
    }

    @Override
    public void memo(String user, String memo) {
        String owner = userHelper.id();
        FriendModel model = find(owner, user);
        if (model.getState() != 2)
            return;

        model.setMemo(memo);
        friendDao.save(model);
        cleanCache(owner, user);
    }

    @Override
    public FriendModel find(String user) {
        return find(userHelper.id(), user);
    }

    private FriendModel find(String owner, String user) {
        String cacheKey = CACHE_OWNER_USER + owner + user;
        FriendModel model = cache.get(cacheKey);
        if (model == null)
            cache.put(cacheKey, model = friendDao.find(owner, user), false);

        return model;
    }

    private void cleanCache(String owner, String user) {
        for (int i = 0; i < 4; i++)
            cache.remove(CACHE_OWNER + owner + i);
        cache.remove(CACHE_OWNER_USER + owner + user);
    }
}
