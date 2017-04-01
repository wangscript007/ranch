package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.message.helper.MessageHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Comparator;

/**
 * @author lpw
 */
@Service(FriendModel.NAME + ".service")
public class FriendServiceImpl implements FriendService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Message message;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MessageHelper messageHelper;
    @Inject
    private FriendDao friendDao;

    @Override
    public JSONArray query(int state) {
        String owner = userHelper.id();
        JSONArray array = modelHelper.toJson(friendDao.query(owner, state, state != 2).getList());
        array = userHelper.fill(array, new String[]{"user"});
        if (state == 2)
            array.sort(Comparator.comparing(this::getCompareName));

        return array;
    }

    private String getCompareName(Object object) {
        JSONObject json = (JSONObject) object;
        String memo = json.getString("memo");
        String name = validator.isEmpty(memo) ? json.getJSONObject("user").getString("nick") : memo;

        return validator.isEmpty(name) ? "" : name;
    }

    @Override
    public JSONObject get(String[] ids) {
        JSONObject json = new JSONObject();
        for (String id : ids) {
            FriendModel friend1 = friendDao.findById(id);
            if (friend1 == null || friend1.getState() != 2)
                continue;

            FriendModel friend2 = friendDao.find(friend1.getUser(), friend1.getOwner());
            if (friend2 == null || friend2.getState() != 2)
                continue;

            json.put(id, modelHelper.toJson(friend1));
        }

        return json;
    }

    @Override
    public void create(String user, String memo) {
        String owner = userHelper.id();
        FriendModel friend = find(owner, user);
        if (friend == null) {
            create(owner, user, null, 0);
            create(user, owner, memo, 1);
        } else if (friend.getState() == 1) {
            pass(owner, user, null, 1);
            pass(user, owner, memo, 0);
        }
    }

    private void create(String owner, String user, String memo, int state) {
        FriendModel friend = new FriendModel();
        friend.setOwner(owner);
        friend.setUser(user);
        friend.setMemo(memo);
        friend.setState(state);
        friend.setCreate(dateTime.now());
        friendDao.save(friend);
        if (state == 1)
            notify(friend, owner, "friend.newcomer", "");
    }

    @Override
    public void pass(String user, String memo) {
        String owner = userHelper.id();
        pass(owner, user, memo, 1);
        pass(user, owner, null, 0);
    }

    private void pass(String owner, String user, String memo, int state) {
        FriendModel friend = find(owner, user);
        if (friend == null || friend.getState() != state)
            return;

        if (!validator.isEmpty(memo))
            friend.setMemo(memo);
        friend.setState(2);
        friendDao.save(friend);
        notify(friend, owner, "friend.pass", message.get(FriendModel.NAME + ".pass"));
    }

    @Override
    public void memo(String user, String memo) {
        String owner = userHelper.id();
        FriendModel friend = find(owner, user);
        if (friend.getState() != 2)
            return;

        friend.setMemo(memo);
        friendDao.save(friend);
    }

    @Override
    public void refuse(String user) {
        String owner = userHelper.id();
        refuse(owner, user, 1);
        refuse(user, owner, 0);
    }

    private void refuse(String owner, String user, int state) {
        FriendModel friend = find(owner, user);
        if (friend == null || friend.getState() != state)
            return;

        friend.setState(3);
        friendDao.save(friend);
        if (state == 0)
            notify(friend, owner, "friend.refuse", message.get(FriendModel.NAME + ".refuse"));
    }

    @Override
    public void blacklist(String user) {
        FriendModel friend = find(user);
        if (friend == null)
            return;

        friend.setState(4);
        friendDao.save(friend);
    }

    @Override
    public FriendModel find(String user) {
        return find(userHelper.id(), user);
    }

    private FriendModel find(String owner, String user) {
        return friendDao.find(owner, user);
    }

    private void notify(FriendModel friend, String receiver, String operate, Object message) {
        JSONObject object = new JSONObject();
        object.put("operate", operate);
        object.put("friend", getJson(friend));
        object.put("message", message);
        messageHelper.notify(MessageHelper.Type.Friend, receiver, object.toJSONString());
    }

    private JSONObject getJson(FriendModel friend) {
        JSONObject object = modelHelper.toJson(friend);
        object.put("user", userHelper.get(friend.getUser()));

        return object;
    }
}
