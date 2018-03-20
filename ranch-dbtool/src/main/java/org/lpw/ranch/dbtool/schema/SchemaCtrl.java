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
            @Validate(validator = Validators.ID, emptyable = true, parameter = "group", failureCode = 2),
            @Validate(validator = SchemaService.VALIDATOR_TYPE, emptyable = true, parameter = "type", failureCode = 5),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return schemaService.query(request.get("group"), request.get("key"), request.get("type"), request.get("ip"), request.get("name"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, emptyable = true, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "group", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 4),
            @Validate(validator = SchemaService.VALIDATOR_TYPE, parameter = "type", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ip", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "ip", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "username", failureCode = 9),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "password", failureCode = 10),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "memo", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = SchemaService.VALIDATOR_EXISTS, emptyable = true, parameter = "id", failureCode = 12),
            @Validate(validator = SchemaService.VALIDATOR_KEY_NOT_EXISTS, parameters = {"id", "key"}, failureCode = 13)
    })
    public Object save() {
        return schemaService.save(request.setToModel(SchemaModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = SchemaService.VALIDATOR_EXISTS, parameter = "id", failureCode = 12)
    })
    public Object delete() {
        schemaService.delete(request.get("id"));

        return "";
    }
}
