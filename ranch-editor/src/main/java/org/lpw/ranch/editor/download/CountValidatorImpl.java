package org.lpw.ranch.editor.download;

import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.buy.BuyService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.lpw.tephra.util.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.Set;

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
    private BuyService buyService;
    @Inject
    private EditorService editorService;
    @Inject
    private DownloadDao downloadDao;
    @Value("${" + DownloadModel.NAME + ".date-max:2}")
    private int dateMax;
    @Value("${" + DownloadModel.NAME + ".vip-date-max:10}")
    private int vipDateMax;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        String template = editorService.findTemplate(parameter);
        if (template == null)
            return false;

        String user = userHelper.id();
        if (buyService.find(user, template) != null)
            return true;

        Set<String> set = downloadDao.editors(user, dateTime.getStart(dateTime.now()));
        set.remove(template);
        int count = set.size();

        return count < dateMax || (count < vipDateMax && userHelper.isVip());
    }

    @Override
    public int getFailureCode(ValidateWrapper validate) {
        return userHelper.isVip() ? 3292 : 3291;
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return DownloadModel.NAME + ".over-date-max";
    }
}
