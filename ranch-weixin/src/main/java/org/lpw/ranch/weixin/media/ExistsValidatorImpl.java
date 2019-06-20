package org.lpw.ranch.weixin.media;

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
    private MediaDao mediaDao;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        return mediaDao.findById(parameter) != null;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return MediaModel.NAME + ".not-exists";
    }
}
