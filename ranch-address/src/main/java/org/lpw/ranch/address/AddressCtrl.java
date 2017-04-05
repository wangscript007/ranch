package org.lpw.ranch.address;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AddressModel.NAME + ".ctrl")
@Execute(name = "/address/", key = AddressModel.NAME, code = "21")
public class AddressCtrl {
    @Inject
    private Request request;
    @Inject
    private AddressService addressService;
}
