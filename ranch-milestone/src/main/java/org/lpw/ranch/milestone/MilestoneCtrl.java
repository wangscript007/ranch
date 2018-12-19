package org.lpw.ranch.milestone;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MilestoneModel.NAME + ".ctrl")
@Execute(name = "/milestone/", key = MilestoneModel.NAME, code = "37")
public class MilestoneCtrl {
    @Inject
    private Request request;
    @Inject
    private MilestoneService milestoneService;

    @Execute(name = "user", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object user() {
        return milestoneService.user();
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 3),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object find() {
        return milestoneService.find(request.get("type"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.ID, parameter = "user", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 3),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS, parameter = "user", failureCode = 4)
    })
    public Object create() {
        return milestoneService.create(request.setToModel(MilestoneModel.class));
    }
}
