package org.lpw.ranch.editor.download;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.lpw.tephra.util.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(DownloadService.VALIDATOR_COUNT)
public class CountValidatorImpl extends ValidatorSupport {
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
    @Inject
    private DownloadDao downloadDao;
    @Value("${" + DownloadModel.NAME + ".date-max:2}")
    private int dateMax;
    @Value("${" + DownloadModel.NAME + ".vip-date-max:10}")
    private int vipDateMax;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        int count = downloadDao.editors(userHelper.id(), dateTime.getStart(dateTime.now())).size();

        return count < dateMax || (count < vipDateMax && userHelper.isVip());
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return DownloadModel.NAME + ".over-date-max";
    }
}
