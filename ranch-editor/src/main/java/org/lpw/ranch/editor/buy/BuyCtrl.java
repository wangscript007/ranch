package org.lpw.ranch.editor.buy;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(BuyModel.NAME + ".ctrl")
@Execute(name = "/editor/buy/", key = BuyModel.NAME, code = "10")
public class BuyCtrl {
    @Inject
    private Request request;
    @Inject
    private BuyService buyService;

    @Execute(name = "purchased")
    public Object purchased() {
        return buyService.purchased(request.getAsArray("editors"));
    }
}
