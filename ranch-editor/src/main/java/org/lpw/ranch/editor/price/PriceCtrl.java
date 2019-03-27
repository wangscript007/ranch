package org.lpw.ranch.editor.price;

import org.lpw.ranch.editor.EditorService;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PriceModel.NAME + ".ctrl")
@Execute(name = "/editor/price/", key = PriceModel.NAME, code = "32")
public class PriceCtrl {
    @Inject
    private Request request;
    @Inject
    private PriceService priceService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return priceService.query(request.get("type"), request.get("group"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 31),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "group", failureCode = 32),
            @Validate(validator = Validators.GREATER_THAN, number = {-1}, parameter = "amount", failureCode = 33),
            @Validate(validator = Validators.GREATER_THAN, number = {-1}, parameter = "vip", failureCode = 34),
            @Validate(validator = Validators.GREATER_THAN, number = {-1}, parameter = "limited", failureCode = 35),
            @Validate(validator = EditorService.VALIDATOR_TYPE_EXISTS, parameter = "type", failureCode = 19),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        priceService.save(request.setToModel(PriceModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.SIGN),
            @Validate(validator = PriceService.VALIDATOR_EXISTS, parameter = "id", failureCode = 36),
            @Validate(validator = PriceService.VALIDATOR_GROUP_NOT_EXISTS, parameter = "id", failureCode = 37)
    })
    public Object delete() {
        priceService.delete(request.get("id"));

        return "";
    }
}
