package org.lpw.ranch.doc;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * @auth lpw
 */
@Controller(DocService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Autowired
    protected DocService docService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return docService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return DocModel.NAME + ".id.not-exists";
    }
}
