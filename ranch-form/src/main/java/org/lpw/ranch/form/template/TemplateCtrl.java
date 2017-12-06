package org.lpw.ranch.form.template;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TemplateModel.NAME + ".ctrl")
@Execute(name = "/form/template/", key = TemplateModel.NAME, code = "0")
public class TemplateCtrl {
    @Inject
    private Request request;
    @Inject
    private TemplateService templateService;
}
