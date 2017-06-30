package org.lpw.ranch.link;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(LinkModel.NAME + ".ctrl")
@Execute(name = "/link/", key = LinkModel.NAME, code = "27")
public class LinkCtrl {
    @Inject
    private Request request;
    @Inject
    private LinkService linkService;

    @Execute(name = "query")
    public Object query() {
        return linkService.query(request.get("type"), request.get("id1"), request.get("id2"));
    }

    @Execute(name = "count")
    public Object count() {
        return linkService.count(request.get("type"), request.get("id1"), request.get("id2"));
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id1", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id2", failureCode = 5)
    })
    public Object find() {
        return linkService.find(request.get("type"), request.get("id1"), request.get("id2"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id1", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "id1", failureCode = 4),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id2", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {36}, parameter = "id2", failureCode = 6)
    })
    public Object save() {
        return linkService.save(request.get("type"), request.get("id1"), request.get("id2"), request.getMap());
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "type", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id1", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "id2", failureCode = 5)
    })
    public Object delete() {
        linkService.delete(request.get("type"), request.get("id1"), request.get("id2"));

        return "";
    }
}
