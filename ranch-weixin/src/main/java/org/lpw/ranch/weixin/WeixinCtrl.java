package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.context.Response;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.util.Xml;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Controller(WeixinModel.NAME + ".ctrl")
@Execute(name = "/weixin/", key = WeixinModel.NAME, code = "24")
public class WeixinCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Xml xml;
    @Inject
    private Message message;
    @Inject
    private Request request;
    @Inject
    private Response response;
    @Inject
    private Templates templates;
    @Inject
    private WeixinService weixinService;

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return weixinService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 3),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 4),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appId", failureCode = 5),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appId", failureCode = 6),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "secret", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "secret", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "token", failureCode = 9),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "mchId", failureCode = 10),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "mchKey", failureCode = 11),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = WeixinService.VALIDATOR_NOT_EXISTS, parameters = {"key", "appId"}, failureCode = 12)

    })
    public Object save() {
        return weixinService.save(request.setToModel(new WeixinModel()));
    }

    @Execute(name = "delete", validates = {
            @Validate(validator = Validators.ID, parameter = "id", failureCode = 1),
            @Validate(validator = Validators.SIGN)
    })
    public Object delete() {
        weixinService.delete(request.get("id"));

        return "";
    }

    @Execute(name = "wx.+", type = Templates.STRING)
    public Object service() {
        String uri = request.getUri();
        String appId = uri.substring(uri.lastIndexOf('/') + 1);
        String echostr = request.get("echostr");
        if (!validator.isEmpty(echostr))
            return weixinService.echo(appId, request.get("signature"), request.get("timestamp"), request.get("nonce"), echostr);

        return "";
    }

    @Execute(name = "auth", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "code", failureCode = 51),
            @Validate(validator = Validators.SIGN),
            @Validate(validator = WeixinService.VALIDATOR_EXISTS, parameter = "key", failureCode = 52)
    })
    public Object auth() {
        return weixinService.auth(request.get("key"), request.get("code"));
    }

    @Execute(name = "prepay-qr-code", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 21),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 22),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 23),
            @Validate(validator = WeixinService.VALIDATOR_EXISTS, parameter = "key", failureCode = 24)
    })
    public Object prepayQrCode() {
        weixinService.prepayQrCode(request.get("key"), request.get("user"), request.get("subject"), request.getAsInt("amount"),
                request.get("notice"), request.getAsInt("size"), request.get("logo"), response.getOutputStream());

        return null;
    }

    @Execute(name = "prepay-qr-code-base64", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 21),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 22),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 23),
            @Validate(validator = WeixinService.VALIDATOR_EXISTS, parameter = "key", failureCode = 24)
    })
    public Object prepayQrCodeBase64() {
        return weixinService.prepayQrCodeBase64(request.get("key"), request.get("user"), request.get("subject"), request.getAsInt("amount"),
                request.get("notice"), request.getAsInt("size"), request.get("logo"));
    }

    @Execute(name = "prepay-app", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "subject", failureCode = 21),
            @Validate(validator = Validators.GREATER_THAN, number = {0}, parameter = "amount", failureCode = 22),
            @Validate(validator = UserHelper.VALIDATOR_EXISTS_OR_SIGN_IN, parameter = "user", failureCode = 23),
            @Validate(validator = WeixinService.VALIDATOR_EXISTS, parameter = "key", failureCode = 24)
    })
    public Object prepayApp() {
        JSONObject object = weixinService.prepayApp(request.get("key"), request.get("user"), request.get("subject"), request.getAsInt("amount"), request.get("notice"));

        return object == null ? templates.get().failure(2427, message.get(WeixinModel.NAME + ".prepay.failure"), null, null) : object;
    }

    @Execute(name = "notice", type = Templates.STRING)
    public Object notice() {
        Map<String, String> map = xml.toMap(request.getFromInputStream(), false);
        String code = weixinService.notice(map.get("appid"), map.get("out_trade_no"), map.get("transaction_id"),
                map.get("total_fee"), map.get("return_code"), map.get("result_code"), map) ? "SUCCESS" : "FAIL";

        return "<xml><return_code><![CDATA[" + code + "]]></return_code></xml>";
    }
}
