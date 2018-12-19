package org.lpw.ranch.editor.speech;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.milestone.helper.MilestoneHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.validate.ValidateWrapper;
import org.lpw.tephra.ctrl.validate.ValidatorSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.sql.Timestamp;

@Controller(SpeechService.VALIDATOR_CREATE)
public class CreateValidatorImpl extends ValidatorSupport {
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MilestoneHelper milestoneHelper;
    @Value("${" + SpeechModel.NAME + ".tryout:3}")
    private int tryout;

    @Override
    public boolean validate(ValidateWrapper validate, String parameter) {
        if (userHelper.isVip())
            return true;

        JSONObject milestone = milestoneHelper.find(SpeechModel.NAME + ".create");
        if (validator.isEmpty(milestone))
            return true;

        Timestamp time = dateTime.toTime(milestone.getString("time"));

        return time != null && System.currentTimeMillis() - time.getTime() <= tryout * TimeUnit.Day.getTime();
    }

    @Override
    protected String getDefaultFailureMessageKey() {
        return SpeechModel.NAME + ".create.overdue";
    }
}
