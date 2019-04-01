package org.lpw.ranch.editor;

import org.lpw.ranch.editor.buy.BuyService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(EditorService.VALIDATOR_EXPORT)
public class ExportValidatoImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private BuyService buyService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        if (userHelper.isVip())
            return true;

        String id = parameter;
        for (int i = 0; i < 10; i++) {
            EditorModel editor = editorService.findById(id);
            if (editor == null)
                return false;

            if (editor.getTemplate() > 0)
                return buyService.find(userHelper.id(), id) != null;

            if (validator.isEmpty(editor.getSource()))
                return false;

            id = editor.getSource();
        }

        return false;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return EditorModel.NAME + ".export.disable";
    }
}
