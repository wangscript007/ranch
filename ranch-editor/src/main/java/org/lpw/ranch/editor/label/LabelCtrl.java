package org.lpw.ranch.editor.label;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LabelModel.NAME + ".ctrl")
@Execute(name = "/editor/label/", key = LabelModel.NAME, code = "0")
public class LabelCtrl {
    @Inject
    private Request request;
    @Inject
    private LabelService labelService;
}
