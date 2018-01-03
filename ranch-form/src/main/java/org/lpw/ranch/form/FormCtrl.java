package org.lpw.ranch.form;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FormModel.NAME + ".ctrl")
@Execute(name = "/form/", key = FormModel.NAME, code = "32")
public class FormCtrl {
    @Inject
    private Request request;
    @Inject
    private FormService formService;
}
