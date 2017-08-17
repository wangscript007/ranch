package org.lpw.ranch.dbtool.table;

import org.lpw.ranch.dbtool.schema.SchemaService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
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

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.ID, parameter = "schema", failureCode = 22),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "group", failureCode = 23),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = SchemaService.VALIDATOR_EXISTS, parameter = "schema", failureCode = 29)
    })
    public Object query() {
        return tableService.query(request.get("schema"), request.get("group"), request.get("name"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.ID, emptyable = true, parameter = "id", failureCode = 21),
            @Validate(validator = Validators.ID, parameter = "schema", failureCode = 22),
            @Validate(validator = Validators.ID, emptyable = true, parameter = "group", failureCode = 23),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 24),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 25),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "memo", failureCode = 26),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = SchemaService.VALIDATOR_EXISTS, parameter = "schema", failureCode = 29)
    })
    public Object save() {
        return tableService.save(request.setToModel(new TableModel()));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 21),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        tableService.delete(request.get("id"));

        return "";
    }
}
