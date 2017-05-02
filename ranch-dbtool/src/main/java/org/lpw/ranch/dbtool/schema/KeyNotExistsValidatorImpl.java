package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(SchemaService.VALIDATOR_KEY_NOT_EXISTS)
public class KeyNotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private SchemaService schemaService;

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        SchemaModel schema = schemaService.findByKey(parameters[1]);

        return schema == null || schema.getId().equals(parameters[0]);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SchemaModel.NAME + ".key.exists";
    }
}
