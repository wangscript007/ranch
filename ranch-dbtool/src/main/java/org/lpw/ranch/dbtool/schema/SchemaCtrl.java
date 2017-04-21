package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(SchemaModel.NAME + ".ctrl")
@Execute(name = "/dbtool/schema/", key = SchemaModel.NAME, code = "23")
public class SchemaCtrl {
    @Inject
    private Request request;
    @Inject
    private SchemaService schemaService;
}
