package org.lpw.ranch.editor.graphic;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GraphicModel.NAME + ".ctrl")
@Execute(name = "/editor/graphic/", key = GraphicModel.NAME, code = "32")
public class GraphicCtrl {
    @Inject
    private Request request;
    @Inject
    private GraphicService graphicService;

    @Execute(name = "query")
    public Object query() {
        return graphicService.query(request.get("type"), request.get("name"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "type", failureCode = 82),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 83),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 84),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "label", failureCode = 85),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        return graphicService.save(request.setToModel(GraphicModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 81),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        graphicService.delete(request.get("id"));

        return "";
    }
}
