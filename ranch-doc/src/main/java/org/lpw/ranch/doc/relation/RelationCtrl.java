package org.lpw.ranch.doc.relation;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RelationModel.NAME + ".ctrl")
@Execute(name = "/doc/refresh/", key = RelationModel.NAME, code = "0")
public class RelationCtrl {
    @Inject
    private Request request;
    @Inject
    private RelationService relationService;
}
