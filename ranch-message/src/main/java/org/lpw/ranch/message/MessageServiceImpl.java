package org.lpw.ranch.message;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(MessageModel.NAME + ".service")
public class MessageServiceImpl implements MessageService {
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MessageDao messageDao;

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
}
