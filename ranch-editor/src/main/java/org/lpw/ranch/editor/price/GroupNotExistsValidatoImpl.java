package org.lpw.ranch.editor.price;

import org.lpw.ranch.editor.EditorService;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(PriceService.VALIDATOR_GROUP_NOT_EXISTS)
public class GroupNotExistsValidatoImpl extends ValidatorSupport {
    @Inject
    private EditorService editorService;
    @Inject
    private PriceDao priceDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return editorService.notExistsGroup(priceDao.findById(parameter).getGroup());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return PriceModel.NAME + ".group.exists";
    }
}
