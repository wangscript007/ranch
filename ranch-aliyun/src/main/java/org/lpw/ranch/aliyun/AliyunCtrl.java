package org.lpw.ranch.aliyun;

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
@Execute(name = "/aliyun/", key = AliyunModel.NAME, code = "147")
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
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "regionId", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "regionId", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "accessKeyId", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "accessKeyId", failureCode = 7),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "accessKeySecret", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "accessKeySecret", failureCode = 9),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        aliyunService.save(request.setToModel(AliyunModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 10),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        aliyunService.delete(request.get("id"));

        return "";
    }
}
