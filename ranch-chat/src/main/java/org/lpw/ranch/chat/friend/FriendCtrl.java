package org.lpw.ranch.chat.friend;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FriendModel.NAME + ".ctrl")
@Execute(name = "/chat/friend/", key = FriendModel.NAME, code = "0")
public class FriendCtrl {
    @Inject
    private Request request;
    @Inject
    private FriendService friendService;
}
