package org.lpw.ranch.comment;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.user.User;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(CommentService.VALIDATOR_DELETE_ENABLE)
public class DeleteEnableValidatorImpl extends ValidatorSupport {
    @Inject
    private User user;
    @Inject
    private CommentService commentService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        CommentModel comment = commentService.findById(parameter);

        return comment != null && comment.getAudit() != Audit.Passed.getValue() && comment.getAuthor().equals(user.sign().getString("id"));
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return CommentModel.NAME + ".delete.disable";
    }
}
