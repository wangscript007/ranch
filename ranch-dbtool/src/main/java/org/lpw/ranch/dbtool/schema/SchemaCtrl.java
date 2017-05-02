package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
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

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return schemaService.query(request.get("group"), request.get("key"), request.get("type"), request.get("ip"), request.get("name"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, emptyable = true, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 3),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return schemaService.save(request.setToModel(new SchemaModel()));
    }
}
