package org.lpw.ranch.category;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(CategoryModel.NAME + ".ctrl")
@Execute(name = "/category/", key = CategoryModel.NAME, code = "148")
public class CategoryCtrl {
    @Inject
    private Request request;
    @Inject
    private CategoryService categoryService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return categoryService.query(request.get("key"));
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 4),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        categoryService.save(request.setToModel(CategoryModel.class));

        return "";
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = CategoryService.LEAF_VALIDATOR, parameters = {"key", "id"}, failureCode = 12)
    })
    public Object delete() {
        categoryService.delete(request.get("id"));

        return "";
    }
}
