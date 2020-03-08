package org.lpw.ranch.user.crosier;

import org.lpw.ranch.user.UserService;
import org.lpw.tephra.ctrl.Failure;
import org.lpw.tephra.ctrl.Interceptor;
import org.lpw.tephra.ctrl.Invocation;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.validate.SignValidator;
import org.lpw.tephra.util.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(CrosierModel.NAME + ".interceptor")
public class CrosierInterceptorImpl implements Interceptor {
    @Inject
    private Logger logger;
    @Inject
    private Header header;
    @Inject
    private Request request;
    @Inject
    private SignValidator signValidator;
    @Inject
    private UserService userService;
    @Inject
    private CrosierService crosierService;
    @Value("${" + CrosierModel.NAME + ".on:false}")
    private boolean on;

    @Override
    public int getSort() {
        return 115;
    }

    @Override
    public Object execute(Invocation invocation) throws Exception {
        if (!on)
            return invocation.invoke();

        if (crosierService.permit(request.getUri(), request.getMap())) {
            signValidator.setSignEnable(false);

            return invocation.invoke();
        }

        logger.warn(null, "未授权用户[{}:{}]访问[{}:{}:{}]。", header.getIp(), userService.sign(), request.getUri(), header.getMap(), request.getMap());

        return Failure.NotPermit;
    }
}
