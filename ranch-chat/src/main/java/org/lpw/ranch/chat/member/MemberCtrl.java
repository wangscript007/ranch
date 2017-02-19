package org.lpw.ranch.chat.member;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MemberModel.NAME + ".ctrl")
@Execute(name = "/chat/member/", key = MemberModel.NAME, code = "0")
public class MemberCtrl {
    @Inject
    private Request request;
    @Inject
    private MemberService memberService;
}
