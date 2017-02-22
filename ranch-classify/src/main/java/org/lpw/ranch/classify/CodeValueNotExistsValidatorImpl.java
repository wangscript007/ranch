package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ClassifyService.VALIDATOR_CODE_VALUE_NOT_EXISTS)
public class CodeValueNotExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private ClassifyService classifyService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        ClassifyModel classify = classifyService.findById(parameter);

        return validate(classify.getId(), classify.getCode(), classify.getValue());
    }

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return validate(parameters.length == 3 ? parameters[2] : null, parameters[0], parameters[1]);
    }

    private boolean validate(String id, String code, String value) {
        JSONObject object = classifyService.find(code, value);

        return object.isEmpty() || object.getString("id").equals(id);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return ClassifyModel.NAME + ".code-value.exists";
    }
}
