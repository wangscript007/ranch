package org.lpw.ranch.access;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.Interceptor;
import org.lpw.tephra.ctrl.Invocation;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Request;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Controller(AccessModel.NAME + ".interceptor")
public class InterceptorImpl implements Interceptor {
    @Inject
    private Header header;
    @Inject
    private Request request;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccessService accessService;

    @Override
    public int getSort() {
        return 34;
    }

    @Override
    public Object execute(Invocation invocation) throws Exception {
        String uri = request.getUri();
        if (!userHelper.signUri().equals(uri)) {
            Map<String, String> map = new HashMap<>(header.getMap());
            accessService.save(map.remove("host"), uri, header.getIp(),
                    map.remove("user-agent"), map.remove("referer"), map);
        }

        return invocation.invoke();
    }
}
