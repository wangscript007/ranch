package org.lpw.ranch.dbtool.column;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ColumnModel.NAME + ".ctrl")
@Execute(name = "/dbtool/column/", key = ColumnModel.NAME, code = "123")
public class ColumnCtrl {
    @Inject
    private Request request;
    @Inject
    private ColumnService columnService;
}
