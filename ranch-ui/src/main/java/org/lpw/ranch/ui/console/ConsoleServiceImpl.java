package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".service")
public class ConsoleServiceImpl implements ConsoleService {
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private Carousel carousel;
    @Inject
    private MetaHelper metaHelper;
    @Inject
    private UserHelper userHelper;
    @Value("${" + ConsoleModel.NAME + ".root:/WEB-INF/ui/}")
    private String root;

    @Override
    public boolean signUp(String domain) {
        return true;
    }

    @Override
    public boolean permit(String domain) {
        return true;
    }

    private boolean permit(JSONObject menu) {
        return !menu.containsKey("grade") || menu.getIntValue("grade") <= userHelper.grade();
    }

    @Override
    public JSONObject service(String service, Map<String, String> parameter) {
        return carousel.service(service, null, parameter, false);
    }
}
