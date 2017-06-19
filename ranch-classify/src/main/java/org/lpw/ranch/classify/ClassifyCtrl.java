package org.lpw.ranch.classify;

import org.lpw.ranch.recycle.RecycleCtrlSupport;
import org.lpw.ranch.recycle.RecycleService;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ClassifyModel.NAME + ".ctrl")
@Execute(name = "/classify/", key = ClassifyModel.NAME, code = "12")
public class ClassifyCtrl extends RecycleCtrlSupport {
    @Inject
    private Templates templates;
    @Inject
    private ClassifyService classifyService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return classifyService.query(request.get("code"), request.get("key"), request.get("value"), request.get("name"));
    }

    @Execute(name = "tree", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 2)
    })
    public Object tree() {
        return classifyService.tree(request.get("code"));
    }

    @Execute(name = "get", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "ids", failureCode = 11)
    })
    public Object get() {
        return classifyService.get(request.getAsArray("ids"));
    }

    @Execute(name = "find", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 4)
    })
    public Object find() {
        return templates.get().success(classifyService.find(request.get("code"), request.get("key")), null);
    }

    @Execute(name = "list", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 2)
    })
    public Object list() {
        return classifyService.list(request.get("code"), request.get("key"), request.get("name"));
    }

    @Execute(name = "create", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "code", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "value", failureCode = 6),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 8),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ClassifyService.VALIDATOR_CODE_KEY_NOT_EXISTS, parameters = {"code", "key"}, failureCode = 10)
    })
    public Object create() {
        return templates.get().success(classifyService.create(request.get("code"), request.get("key"), request.get("value"), request.get("name"), request.getMap()), null);
    }

    @Execute(name = "modify", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "code", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "value", failureCode = 6),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 8),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ClassifyService.VALIDATOR_EXISTS, parameter = "id", failureCode = 9),
            @Validate(validator = ClassifyService.VALIDATOR_CODE_KEY_NOT_EXISTS, parameters = {"code", "key", "id"}, failureCode = 10)
    })
    public Object modify() {
        return templates.get().success(classifyService.modify(request.get("id"), request.get("code"), request.get("key"), request.get("value"), request.get("name"), request.getMap()), null);
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "code", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "value", failureCode = 6),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 8),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return templates.get().success(classifyService.save(request.get("code"), request.get("key"), request.get("value"), request.get("name"), request.getMap()), null);
    }

    @Execute(name = "refresh", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object refresh() {
        classifyService.refresh();

        return "";
    }

    @Execute(name = "restore", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 86),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = ClassifyService.VALIDATOR_EXISTS, parameter = "id", failureCode = 9),
            @Validate(validator = ClassifyService.VALIDATOR_CODE_KEY_NOT_EXISTS, parameter = "id", failureCode = 10)
    })
    public Object restore() {
        return super.restore();
    }

    @Override
    protected RecycleService getRecycleService() {
        return classifyService;
    }
}
