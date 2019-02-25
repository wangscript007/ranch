package org.lpw.ranch.editor;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(EditorService.VALIDATOR_COPY_VIP)
public class CopyVipValidatoImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private EditorService editorService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return userHelper.isVip() || editorService.findById(parameter).getTemplate() != 2;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return EditorModel.NAME + ".copy.not-vip";
    }
}
