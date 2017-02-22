package org.lpw.ranch.chat.friend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(FriendModel.NAME + ".service")
public class FriendServiceImpl implements FriendService {
    private static final String CACHE_OWNER = FriendModel.NAME + ".service.owner:";

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
    public JSONArray query() {
        String owner = userHelper.id();
        String cacheKey = CACHE_OWNER + owner;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = modelHelper.toJson(friendDao.query(owner).getList());
            cache.put(cacheKey, array = userHelper.fill(array, new String[]{"friend"}), false);
        }

        return array;
    }

    @Override
    public void create(String friend, String note) {
        JSONObject user = userHelper.get(friend);
        if (user.isEmpty())
            user = userHelper.find(friend);
        if (user.isEmpty())
            return;

        String owner = userHelper.id();
        String friendId = user.getString("id");
        JSONArray array = query();
        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("id").equals(friendId)) {
                if (object.getIntValue("state") == 1)
                    pass(friendId, null);

                return;
            }
        }

        create(owner, friendId, null, 0);
        create(friendId, owner, note, 1);
    }

    private void create(String owner, String friend, String note, int state) {
        FriendModel model = new FriendModel();
        model.setOwner(owner);
        model.setFriend(friend);
        model.setNote(note);
        model.setState(state);
        model.setCreate(dateTime.now());
        friendDao.save(model);
        cache.remove(CACHE_OWNER + owner);
    }

    @Override
    public void pass(String friend, String note) {
        String owner = userHelper.id();
        pass(owner, friend, note);
        pass(friend, owner, null);
    }

    private void pass(String owner, String friend, String note) {
        FriendModel model = friendDao.find(owner, friend);
        if (model == null)
            return;

        if (!validator.isEmpty(note))
            model.setNote(note);
        model.setState(2);
        friendDao.save(model);
        cache.remove(CACHE_OWNER + owner);
    }
}
