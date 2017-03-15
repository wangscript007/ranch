package org.lpw.ranch.snapshot;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(SnapshotModel.NAME + ".ctrl")
@Execute(name = "/snapshot/", key = SnapshotModel.NAME, code = "19")
public class SnapshotCtrl {
    @Inject
    private Request request;
    @Inject
    private SnapshotService snapshotService;

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 1),
            @Validate(validator = Validators.SIGN)
    })
    public Object get() {
        return snapshotService.get(request.getAsArray("ids"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "data", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "content", failureCode = 3),
            @Validate(validator = Validators.SIGN)
    })
    public Object create() {
        return snapshotService.create(request.get("data"), request.get("content"));
    }
}
