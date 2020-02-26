package org.lpw.ranch.category;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(CategoryService.LEAF_VALIDATOR)
public class LeafValidatorImpl extends ValidatorSupport {
    @Inject
    private CategoryDao categoryDao;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return categoryDao.count(parameters[0], parameters[1]) == 0;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return CategoryModel.NAME + ".not-leaf";
    }
}
