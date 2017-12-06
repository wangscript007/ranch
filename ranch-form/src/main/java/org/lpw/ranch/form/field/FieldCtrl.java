package org.lpw.ranch.form.field;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FieldModel.NAME + ".ctrl")
@Execute(name = "/form/field/", key = FieldModel.NAME, code = "0")
public class FieldCtrl {
    @Inject
    private Request request;
    @Inject
    private FieldService fieldService;
}
