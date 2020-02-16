package org.lpw.ranch.ui.console;

import org.lpw.tephra.ctrl.Interceptor;
import org.lpw.tephra.ctrl.Invocation;
import org.lpw.tephra.ctrl.validate.SignValidator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

@Controller(ConsoleModel.NAME + ".interceptor")
public class ConsoleInterceptor implements Interceptor {
    @Inject
    private SignValidator signValidator;

    @Override
    public int getSort() {
        return 0;
    }

    @Override
    public Object execute(Invocation invocation) throws Exception {
        signValidator.setSignEnable(false);

        return invocation.invoke();
    }
}
