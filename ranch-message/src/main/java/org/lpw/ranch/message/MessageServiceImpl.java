package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashSet;
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
    @Value("${" + MessageModel.NAME + ".size:100}")
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
    public JSONArray newest(long time) {
        JSONArray groups = carousel.service(groupKey + ".query-by-user", null, null, false, JSONArray.class);
        Set<String> receivers = new HashSet<>();
        for (int i = 0, size = groups.size(); i < size; i++)
            receivers.add(groups.getJSONObject(i).getString("id"));

        JSONArray array = new JSONArray();
        messageDao.query(new Timestamp(time), userHelper.id(), receivers, size).getList().forEach(message -> {
        });

        return array;
    }
}
