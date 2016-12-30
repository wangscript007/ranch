package org.lpw.ranch.doc;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @auth lpw
 */
@Controller(DocService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private DocService docService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return docService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return DocModel.NAME + ".id.not-exists";
    }
}
