package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.last.helper.LastHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Service(MessageModel.NAME + ".service")
public class MessageServiceImpl implements MessageService {
    private static final String LAST_TYPE_NEWEST = MessageModel.NAME + ".newest";

    @Inject
    private DateTime dateTime;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Carousel carousel;
    @Inject
    private UserHelper userHelper;
    @Inject
    private LastHelper lastHelper;
    @Inject
    private MessageDao messageDao;
    @Value("${ranch.friend.key:ranch.friend}")
    private String friendKey;
    @Value("${ranch.group.key:ranch.group}")
    private String groupKey;
    @Value("${" + MessageModel.NAME + ".deadline:2592000}")
    private int deadline;

    @Override
    public String send(int type, String receiver, int format, String content, int deadline, String code) {
        MessageModel message = messageDao.findByCode(code);
        if (message != null)
            return message.getId();

        return type == 1 ? sendToGroup(receiver, format, content, deadline, code) : sendToFriend(receiver, format, content, deadline, code);
    }

    private String sendToFriend(String receiver, int format, String content, int deadline, String code) {
        JSONObject friend = carousel.get(friendKey + ".get", receiver);
        if (friend.size() <= 1) {
            if (logger.isDebugEnable())
                logger.debug("好友[{}]信息[{}]不存在，消息发送失败！", receiver, friend.toJSONString());

            return null;
        }

        return send(userHelper.id(), 0, friend.getString("user"), format, content, deadline, code);
    }

    private String sendToGroup(String receiver, int format, String content, int deadline, String code) {
        String user = userHelper.id();
        Map<String, String> parameter = new HashMap<>();
        parameter.put("group", receiver);
        parameter.put("user", user);
        JSONObject member = carousel.service(groupKey + ".member.find", null, parameter, false, JSONObject.class);
        if (member.isEmpty()) {
            if (logger.isDebugEnable())
                logger.debug("群组[{}]成员信息不存在，消息发送失败！", receiver);

            return null;
        }

        return send(member.getString("id"), 1, receiver, format, content, deadline, code);
    }

    @Override
    public String notify(int type, String receiver, String content, int deadline, String code) {
        MessageModel message = messageDao.findByCode(code);
        if (message != null)
            return message.getId();

        return send("", type, receiver, 9, content, deadline, code);
    }

    private String send(String sender, int type, String receiver, int format, String content, int deadline, String code) {
        MessageModel message = new MessageModel();
        message.setSender(sender);
        message.setType(type);
        message.setReceiver(receiver);
        message.setFormat(format);
        message.setContent(content);
        message.setTime(dateTime.now());
        message.setDeadline(new Timestamp(System.currentTimeMillis() + 1000L * (deadline > 0 ? deadline : this.deadline)));
        message.setCode(code);
        messageDao.save(message);

        return message.getId();
    }

    @Override
    public JSONObject newest(long time) {
        JSONObject object = new JSONObject();
        object.put("time", System.currentTimeMillis());
        object.put("messages", query(getGroups(), time));

        return object;
    }

    private Set<String> getGroups() {
        Set<String> set = new HashSet<>();
        JSONArray array = carousel.service(groupKey + ".query-by-user", null, null, false, JSONArray.class);
        for (int i = 0, size = array.size(); i < size; i++)
            set.add(array.getJSONObject(i).getString("id"));

        return set;
    }

    private JSONArray query(Set<String> groups, long time) {
        Map<String, List<MessageModel>> map = new HashMap<>();
        List<String> list = new ArrayList<>();
        String sender = userHelper.id();
        messageDao.query(lastTime(time), dateTime.now(), sender, groups).getList().forEach(message -> {
            String key;
            if (message.getFormat() == 9)
                key = "notify";
            else
                key = message.getReceiver().equals(sender) ? message.getSender() : message.getReceiver();
            List<MessageModel> messages = map.get(key);
            if (messages == null) {
                messages = new ArrayList<>();
                list.add(key);
            }
            messages.add(message);
            map.put(key, messages);
        });

        JSONArray array = new JSONArray();
        list.forEach(id -> {
            boolean group = groups.contains(id);
            JSONObject object = new JSONObject();
            object.put("id", group || id.equals("") || id.equals("notify") ? id : findFriend(id));
            object.put("group", group);
            object.put("list", modelHelper.toJson(map.get(id)));
            array.add(object);
        });

        return array;
    }

    private Timestamp lastTime(long time) {
        if (time <= 0) {
            JSONObject last = lastHelper.find(LAST_TYPE_NEWEST);
            time = last.isEmpty() ? System.currentTimeMillis() : last.getLongValue("time");
        }
        lastHelper.save(LAST_TYPE_NEWEST, null);

        return new Timestamp(time);
    }

    private String findFriend(String id) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("user", id);
        JSONObject friend = carousel.service(friendKey + ".find", null, parameter, false, JSONObject.class);

        return friend.isEmpty() ? id : friend.getString("id");
    }
}
