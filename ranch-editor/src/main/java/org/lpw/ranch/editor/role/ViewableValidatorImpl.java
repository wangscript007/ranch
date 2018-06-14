package org.lpw.ranch.editor.role;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(RoleService.VALIDATOR_VIEWABLE)
public class ViewableValidatorImpl extends ValidatorSupport {
    @Inject
    private UserHelper userHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private RoleService roleService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return viewable(null, parameter) || roleService.hasType(null, parameter, RoleService.Type.Viewer);
    }

    @Override
    public boolean validate(ValidateWrapper validate, String[] parameters) {
        return viewable(parameters[0], parameters[1]) || roleService.hasType(parameters[0], parameters[1], RoleService.Type.Viewer);
    }

    private boolean viewable(String user, String editor) {
        JSONObject object = user == null ? userHelper.sign() : userHelper.get(user);
        int grade = object.getIntValue("grade");

        return (grade >= 50 && grade <= 99) || editorService.findById(editor).getType().equals("template");
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return RoleModel.NAME + ".not-viewer";
    }
}
