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
    private static final String CACHE_OWNER_FRIEND = FriendModel.NAME + ".service.owner-friend:";

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
            array = userHelper.fill(array, new String[]{"friend"});
            if (state == 2)
                array.sort(Comparator.comparing(this::getCompareName));
            cache.put(cacheKey, array, false);
        }

        return array;
    }

    private String getCompareName(Object object) {
        JSONObject json = (JSONObject) object;
        String memo = json.getString("memo");
        String name = validator.isEmpty(memo) ? json.getJSONObject("friend").getString("nick") : memo;

        return validator.isEmpty(name) ? "" : name;
    }

    @Override
    public void create(String friend, String memo) {
        String owner = userHelper.id();
        FriendModel model = find(owner, friend);
        if (model == null) {
            create(owner, friend, null, 0);
            create(friend, owner, memo, 1);
        } else if (model.getState() == 1) {
            pass(owner, friend, null, 1);
            pass(friend, owner, memo, 0);
        }
    }

    private void create(String owner, String friend, String memo, int state) {
        FriendModel model = new FriendModel();
        model.setOwner(owner);
        model.setFriend(friend);
        model.setMemo(memo);
        model.setState(state);
        model.setCreate(dateTime.now());
        friendDao.save(model);
        cache.remove(CACHE_OWNER + owner);
        cache.remove(CACHE_OWNER_FRIEND + owner + friend);
    }

    @Override
    public void pass(String friend, String memo) {
        String owner = userHelper.id();
        pass(owner, friend, memo, 1);
        pass(friend, owner, null, 0);
    }

    private void pass(String owner, String friend, String memo, int state) {
        FriendModel model = find(owner, friend);
        if (model == null || model.getState() != state)
            return;

        if (!validator.isEmpty(memo))
            model.setMemo(memo);
        model.setState(2);
        friendDao.save(model);
        cache.remove(CACHE_OWNER + owner);
        cache.remove(CACHE_OWNER_FRIEND + owner + friend);
    }

    @Override
    public void memo(String friend, String memo) {
        String owner = userHelper.id();
        FriendModel model = find(owner, friend);
        if (model.getState() != 2)
            return;

        model.setMemo(memo);
        friendDao.save(model);
        cache.remove(CACHE_OWNER + owner);
        cache.remove(CACHE_OWNER_FRIEND + owner + friend);
    }

    @Override
    public FriendModel find(String friend) {
        return find(userHelper.id(), friend);
    }

    private FriendModel find(String owner, String friend) {
        String cacheKey = CACHE_OWNER_FRIEND + owner + friend;
        FriendModel model = cache.get(cacheKey);
        if (model == null)
            cache.put(cacheKey, model = friendDao.find(owner, friend), false);

        return model;
    }
}
