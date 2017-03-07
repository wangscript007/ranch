package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
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
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Carousel carousel;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MessageDao messageDao;
    @Value("${ranch.group.key:ranch.group}")
    private String groupKey;
    @Value("${" + MessageModel.NAME + ".size:200}")
    private int size;

    @Override
    public void send(int type, String receiver, int format, String content) {
        MessageModel message = new MessageModel();
        message.setSender(userHelper.id());
        message.setType(type);
        message.setReceiver(receiver);
        message.setFormat(format);
        message.setContent(content);
        message.setTime(dateTime.now());
        messageDao.save(message);
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
        messageDao.query(new Timestamp(time), sender, groups, size).getList().forEach(message -> {
            String key = message.getReceiver().equals(sender) ? message.getSender() : message.getReceiver();
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
            JSONObject object = new JSONObject();
            object.put("id", id);
            object.put("group", groups.contains(id));
            object.put("list", modelHelper.toJson(map.get(id)));
            array.add(object);
        });

        return array;
    }
}
