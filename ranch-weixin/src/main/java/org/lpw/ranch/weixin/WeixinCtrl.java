package org.lpw.ranch.weixin;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.template.Templates;
import org.lpw.tephra.ctrl.validate.Validate;
import org.lpw.tephra.ctrl.validate.Validators;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(WeixinModel.NAME + ".ctrl")
@Execute(name = "/weixin/", key = WeixinModel.NAME, code = "24")
public class WeixinCtrl {
    @Inject
    private Validator validator;
    @Inject
    private Request request;
    @Inject
    private WeixinService weixinService;

    @Execute(name = "wx.+", type = Templates.STRING)
    public Object service() {
        String uri = request.getUri();
        String appId = uri.substring(uri.lastIndexOf('/') + 1);
        String echostr = request.get("echostr");
        if (!validator.isEmpty(echostr))
            return weixinService.echo(appId, request.get("signature"), request.get("timestamp"), request.get("nonce"), echostr);

        return null;
    }

    @Execute(name = "query", validates = {
            @Validate(validator = Validators.SIGN)
    })
    public Object query() {
        return weixinService.query();
    }

    @Execute(name = "save", validates = {
            @Validate(validator = Validators.NOT_EMPTY, parameter = "key", failureCode = 1),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "key", failureCode = 2),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "name", failureCode = 3),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "appId", failureCode = 4),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "appId", failureCode = 5),
            @Validate(validator = Validators.NOT_EMPTY, parameter = "secret", failureCode = 6),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "secret", failureCode = 7),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "token", failureCode = 8),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "mchId", failureCode = 9),
            @Validate(validator = Validators.MAX_LENGTH, number = {100}, parameter = "mchKey", failureCode = 10),
            @Validate(validator = Validators.SIGN)
    })
    public Object save() {
        weixinService.save(request.setToModel(new WeixinModel()));

        return "";
    }
}
