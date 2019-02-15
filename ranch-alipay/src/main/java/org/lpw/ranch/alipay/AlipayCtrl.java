package org.lpw.ranch.alipay;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Controller(AlipayModel.NAME + ".ctrl")
@Execute(name = "/alipay/", key = AlipayModel.NAME, code = "26")
public class AlipayCtrl {
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Templates templates;
    @Inject
    private AlipayService alipayService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN, string = {"ranch-alipay"})
    })
    public Object query() {
        return alipayService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appId", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appId", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "privateKey", failureCode = 6),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "publicKey", failureCode = 7),
            @Validate(validator = Validators.SIGN, string = {"ranch-alipay"}),
            @Validate(validator = AlipayService.VALIDATOR_NOT_EXISTS, parameters = {"key", "appId"}, failureCode = 8)
    })
    public Object save() {
        return alipayService.save(request.setToModel(AlipayModel.class));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 9),
            @Validate(validator = Validators.SIGN, string = {"ranch-alipay"})
    })
    public Object delete() {
        alipayService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "quick-wap-pay", type = Templates.FREEMARKER, template = "prepay", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 11),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 12),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "billNo", failureCode = 16),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 13),
            @Validate(validator = AlipayService.VALIDATOR_EXISTS, parameter = "key", failureCode = 14)
    })
    public Object quickWapPay() {
        Map<String, String> map = new HashMap<>();
        map.put("html", alipayService.quickWapPay(request.get("key"), request.get("user"), request.get("subject"),
                request.getAsInt("amount"), request.get("billNo"), request.get("notice"), request.get("returnUrl")));

        return map;
    }

    @Execute(name = "fast-instant-trade-pay", type = Templates.FREEMARKER, template = "prepay", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 11),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 12),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "billNo", failureCode = 16),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 13),
            @Validate(validator = AlipayService.VALIDATOR_EXISTS, parameter = "key", failureCode = 14)
    })
    public Object fastInstantTradePay() {
        Map<String, String> map = new HashMap<>();
        map.put("html", alipayService.fastInstantTradePay(request.get("key"), request.get("user"), request.get("subject"),
                request.getAsInt("amount"), request.get("billNo"), request.get("notice"), request.get("returnUrl")));

        return map;
    }

    @Execute(name = "quick-msecurity-pay", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 11),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 12),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "billNo", failureCode = 16),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 13),
            @Validate(validator = AlipayService.VALIDATOR_EXISTS, parameter = "key", failureCode = 14)
    })
    public Object quickMsecurityPay() {
        return alipayService.quickMsecurityPay(request.get("key"), request.get("user"), request.get("subject"),
                request.getAsInt("amount"), request.get("billNo"), request.get("notice"));
    }

    @Execute(name = "notice", type = Templates.STRING)
    public Object notice() {
        return alipayService.notice(request.get("app_id"), request.get("out_trade_no"), request.get("trade_no"),
                request.get("total_amount"), request.get("trade_status"), request.getMap()) ? "success" : "failure";
    }

    @Execute(name = "transfer", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "account", failureCode = 11),
            @Validate(validator = Validators.MAX_LENGTH, parameter = "account", failureCode = 15),
            @Validate(validator = Validators.GREATER_THAN, number = {9}, parameter = "amount", failureCode = 12),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "billNo", failureCode = 16),
            @Validate(validator = Validators.SIGN, string = {"ranch-alipay"}),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 13),
            @Validate(validator = AlipayService.VALIDATOR_EXISTS, parameter = "key", failureCode = 14)
    })
    public Object transfer() {
        return alipayService.transfer(request.get("key"), request.get("user"), request.get("account"), request.getAsInt("amount"),
                request.get("billNo"), request.get("realName"), request.get("showName"), request.get("remark"), request.get("notice"),
                request.getMap());
    }
}
