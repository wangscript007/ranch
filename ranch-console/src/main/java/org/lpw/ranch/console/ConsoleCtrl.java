package org.lpw.ranch.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.execute.Execute;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller("ranch.console.ctrl")
@Execute(name = "/console/")
public class ConsoleCtrl {
    @Inject
    private Request request;

    @Execute(name = "menu")
    public Object menu() {
        JSONArray menus = new JSONArray();
        for (int i = 0; i < 5; i++) {
            JSONObject menu = new JSONObject();
            menu.put("name", "主菜单" + i);
            JSONArray items = new JSONArray();
            for (int j = 0; j < 5; j++) {
                JSONObject item = new JSONObject();
                item.put("name", "子菜单" + i + j);
                item.put("service", "service:" + i + "-" + j);
                items.add(item);
            }
            menu.put("items", items);
            menus.add(menu);
        }

        return menus;
    }

    @Execute(name = "service")
    public Object service() {
        JSONObject object = new JSONObject();
        String service = request.get("service");
        object.put("service", service);
        object.put("page", service.contains("0") ? "grid" : "form");

        return object;
    }
}
