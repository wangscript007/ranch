package org.lpw.ranch.weixin.template;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(TemplateService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private TemplateDao templateDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return templateDao.find(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return TemplateModel.NAME + ".not-exists";
    }
}
