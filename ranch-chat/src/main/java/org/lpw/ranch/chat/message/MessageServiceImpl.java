package org.lpw.ranch.chat.message;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(MessageModel.NAME + ".service")
public class MessageServiceImpl implements MessageService {
    @Inject
    private MessageDao messageDao;
}
