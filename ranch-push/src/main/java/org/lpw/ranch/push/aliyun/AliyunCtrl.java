package org.lpw.ranch.push.aliyun;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AliyunModel.NAME + ".ctrl")
@Execute(name = "/push/aliyun/", key = AliyunModel.NAME, code = "131")
public class AliyunCtrl {
    @Inject
    private Request request;
    @Inject
    private AliyunService aliyunService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return aliyunService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appCode", failureCode = 41),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appCode", failureCode = 42),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "keyId", failureCode = 43),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "keyId", failureCode = 44),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "keySecret", failureCode = 45),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "keySecret", failureCode = 46),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appKey", failureCode = 47),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appKey", failureCode = 48),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return aliyunService.save(request.get("appCode"), request.get("keyId"), request.get("keySecret"), request.get("appKey"));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 49),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        aliyunService.delete(request.get("id"));

        return "";
    }
}
