package org.lpw.ranch.account;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AccountModel.NAME + ".ctrl")
@Execute(name = "/account/", key = AccountModel.NAME, code = "0")
public class AccountCtrl {
    @Inject
    private Request request;
    @Inject
    private AccountService accountService;
}
