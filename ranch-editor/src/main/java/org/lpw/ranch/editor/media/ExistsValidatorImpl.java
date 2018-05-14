package org.lpw.ranch.editor.media;

import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MediaService.VALIDATOR_EXISTS)
public class ExistsValidatorImpl extends ValidatorSupport {
    @Inject
    private MediaService mediaService;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return mediaService.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MediaModel.NAME + ".not-exists";
    }
}
