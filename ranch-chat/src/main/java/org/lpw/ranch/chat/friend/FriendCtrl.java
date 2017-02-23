package org.lpw.ranch.chat.friend;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FriendModel.NAME + ".ctrl")
@Execute(name = "/chat/friend/", key = FriendModel.NAME, code = "16")
public class FriendCtrl {
    @Inject
    private Request request;
    @Inject
    private FriendService friendService;

    @Execute(name = "query", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object query() {
        return friendService.query();
    }
}
