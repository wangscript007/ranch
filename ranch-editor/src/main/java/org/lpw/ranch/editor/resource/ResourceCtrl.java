package org.lpw.ranch.editor.resource;

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
@Controller(ResourceModel.NAME + ".ctrl")
@Execute(name = "/editor/resource/", key = ResourceModel.NAME, code = "32")
public class ResourceCtrl {
    @Inject
    private Request request;
    @Inject
    private ResourceService resourceService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return resourceService.query(request.get("type"), request.get("name"), request.get("label"),
                request.getAsInt("state", -1), request.get("uid"));
    }

    @Execute(name = "onsale")
    public Object onsale() {
        return resourceService.onsale(request.get("type"), request.get("label"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 83),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 84),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 85),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "label", failureCode = 86),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "uri", failureCode = 87),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "thumbnail", failureCode = 88),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = ResourceService.VALIDATOR_EDITABLE, emptyable = true, parameter = "id", failureCode = 89)
    })
    public Object save() {
        return resourceService.save(request.setToModel(ResourceModel.class));
    }

    @Execute(name = "pass", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ResourceService.VALIDATOR_EXISTS, parameter = "id", failureCode = 82)
    })
    public Object pass() {
        return resourceService.state(request.get("id"), 1);
    }

    @Execute(name = "reject", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ResourceService.VALIDATOR_EXISTS, parameter = "id", failureCode = 82)
    })
    public Object reject() {
        return resourceService.state(request.get("id"), 2);
    }

    @Execute(name = "sale", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ResourceService.VALIDATOR_EXISTS, parameter = "id", failureCode = 82)
    })
    public Object sale() {
        return resourceService.state(request.get("id"), 3);
    }

    @Execute(name = "nonsale", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ResourceService.VALIDATOR_EXISTS, parameter = "id", failureCode = 82)
    })
    public Object nonsale() {
        return resourceService.state(request.get("id"), 4);
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ResourceService.VALIDATOR_EDITABLE, emptyable = true, parameter = "id", failureCode = 89)
    })
    public Object delete() {
        resourceService.delete(request.get("id"));

        return "";
    }
}
