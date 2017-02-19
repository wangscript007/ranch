package org.lpw.ranch.chat.message;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MessageModel.NAME + ".ctrl")
@Execute(name = "/chat/message/", key = MessageModel.NAME, code = "0")
public class MessageCtrl {
    @Inject
    private Request request;
    @Inject
    private MessageService messageService;
}
