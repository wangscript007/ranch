package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(SchemaService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private SchemaService schemaService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return schemaService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SchemaModel.NAME + ".not-exists";
    }
}
