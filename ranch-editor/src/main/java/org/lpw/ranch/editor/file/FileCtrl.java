package org.lpw.ranch.editor.file;

import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.download.DownloadService;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(FileModel.NAME + ".ctrl")
@Execute(name = "/editor/file/", key = FileModel.NAME, code = "32")
public class FileCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private FileService fileService;

    @Execute(name = "download", validates = {
            @Validate(validator = Validators.ID, parameter = "editor", failureCode = 1),
            @Validate(validator = UserHelper.VALIDATOR_SIGN_IN),
            @Validate(validator = EditorService.VALIDATOR_EXISTS, parameter = "editor", failureCode = 2),
            @Validate(validator = RoleService.VALIDATOR_VIEWABLE, parameter = "editor", failureCode = 41),
            @Validate(validator = DownloadService.VALIDATOR_COUNT, parameter = "editor")
    })
    public Object download() {
        String url = fileService.download(request.get("editor"), request.get("type"), request.get("email"));

        return url == null ? templates.get().failure(3292, message.get(FileModel.NAME + ".not-exists"), null, null) : url;
    }
}
