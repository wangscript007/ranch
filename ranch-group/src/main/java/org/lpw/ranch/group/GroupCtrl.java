package org.lpw.ranch.group;

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
@Controller(GroupModel.NAME + ".ctrl")
@Execute(name = "/group/", key = GroupModel.NAME, code = "17")
public class GroupCtrl {
    @Inject
    private Request request;
    @Inject
    private GroupService groupService;

    @Execute(name = "query-by-user", validates = {
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object queryByUser() {
        return groupService.queryByUser();
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 8),
            @Validate(validator = Validators.SIGN)
    })
    public Object get() {
        return groupService.get(request.getAsArray("ids"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "note", failureCode = 3),
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "audit", failureCode = 4),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN)
    })
    public Object create() {
        return groupService.create(request.get("name"), request.get("note"), request.getAsInt("audit"));
    }

    @Execute(name = "name", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 2),
            @Validate(validator = GroupService.VALIDATOR_MODIFY_ENABLE, parameter = "id", failureCode = 5)
    })
    public Object name() {
        return groupService.name(request.get("id"), request.get("name"));
    }

    @Execute(name = "portrait", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "portrait", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "portrait", failureCode = 7),
            @Validate(validator = GroupService.VALIDATOR_MODIFY_ENABLE, parameter = "id", failureCode = 5)
    })
    public Object portrait() {
        return groupService.portrait(request.get("id"), request.get("portrait"));
    }

    @Execute(name = "note", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "note", failureCode = 3),
            @Validate(validator = GroupService.VALIDATOR_MODIFY_ENABLE, parameter = "id", failureCode = 5)
    })
    public Object note() {
        return groupService.note(request.get("id"), request.get("note"));
    }

    @Execute(name = "audit", validates = {
            @Validate(validator = Validators.BETWEEN, number = {0, 1}, parameter = "audit", failureCode = 4),
            @Validate(validator = GroupService.VALIDATOR_MODIFY_ENABLE, parameter = "id", failureCode = 5)
    })
    public Object audit() {
        return groupService.audit(request.get("id"), request.getAsInt("audit"));
    }
}
