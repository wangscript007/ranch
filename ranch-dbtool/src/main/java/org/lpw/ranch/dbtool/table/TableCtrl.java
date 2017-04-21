package org.lpw.ranch.dbtool.table;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TableModel.NAME + ".ctrl")
@Execute(name = "/dbtool/table/", key = TableModel.NAME, code = "23")
public class TableCtrl {
    @Inject
    private Request request;
    @Inject
    private TableService tableService;
}
