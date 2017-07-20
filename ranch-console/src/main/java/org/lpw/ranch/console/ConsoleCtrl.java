package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ConsoleModel.NAME + ".ctrl")
@Execute(name = "/console/")
public class ConsoleCtrl {
    @Inject
    private Request request;
    @Inject
    private ConsoleService consoleService;

    @Execute(name = "menu")
    public Object menu() {
        return consoleService.menus();
    }

    @Execute(name = "meta")
    public Object meta() {
        return consoleService.meta(request.get("service"));
    }

    @Execute(name = "service")
    public Object service() {
        JSONObject object = new JSONObject();
        String service = request.get("service");
        object.put("service", service);
        if (service.equals("dashboard"))
            object.put("page", service);
        else
            object.put("page", service.contains("0") ? "grid" : "form");

        return object;
    }
}
