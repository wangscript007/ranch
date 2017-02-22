package org.lpw.ranch.chat.friend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
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
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private FriendDao friendDao;

    @Override
    public JSONArray query() {
        String cacheKey = CACHE_OWNER + userHelper.id();
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = modelHelper.toJson(friendDao.query(userHelper.id()).getList());
            cache.put(cacheKey, array = userHelper.fill(array, new String[]{"friend"}), false);
        }

        return array;
    }

    @Override
    public void create(String friend, String note) {
        JSONArray array = query();
        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            if (object.getString("id").equals(friend) || object.getString("code").equals(friend)) {
                return;
            }
        }

        create(userHelper.id(), friend, note, 0);
        create(friend, userHelper.id(), note, 1);
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
    }
}
