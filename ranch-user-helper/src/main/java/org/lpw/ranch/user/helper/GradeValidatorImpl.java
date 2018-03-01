package org.lpw.ranch.user.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(UserHelper.VALIDATOR_GRADE)
public class GradeValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        JSONObject user = validator.isEmpty(parameter) ? userHelper.sign() : userHelper.get(parameter).getJSONObject(parameter);
        if (validator.isEmpty(user))
            return false;

        int grade = user.getIntValue("grade");

        return (validate.getNumber()[0] < 0 || validate.getNumber()[0] <= grade)
                && (validate.getNumber()[1] < 0 || validate.getNumber()[1] >= grade);
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return "ranch.user.helper.grade.illegal";
    }
}
