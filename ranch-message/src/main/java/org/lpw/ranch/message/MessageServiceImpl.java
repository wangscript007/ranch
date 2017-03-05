package org.lpw.ranch.message;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;

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
    private UserHelper userHelper;
    @Inject
    private MessageDao messageDao;
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
        String sender = userHelper.id();
        JSONArray array = modelHelper.toJson(messageDao.query(new Timestamp(time), sender, null, size).getList());

        return array;
    }
}
