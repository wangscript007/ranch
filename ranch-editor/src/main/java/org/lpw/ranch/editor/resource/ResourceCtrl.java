package org.lpw.ranch.editor.resource;

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
        return resourceService.query(request.get("type"), request.get("name"), request.getAsInt("state"), request.get("uid"));
    }

    @Execute(name = "onsale")
    public Object onsale() {
        return resourceService.onsale(request.get("type"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 82),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 83),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 84),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "label", failureCode = 85),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return resourceService.save(request.setToModel(ResourceModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        resourceService.delete(request.get("id"));

        return "";
    }
}
