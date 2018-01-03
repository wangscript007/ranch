package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.smtp")
public class SmtpSenderImpl implements PushSender, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private Json json;
    @Value("${" + PushModel.NAME + ".sender.smtp:}")
    private String smtp;
    private List<Map<String, String>> list;

    @Override
    public String getName() {
        return "smtp";
    }

    @Override
    public boolean send(String receiver, String subject, String content) {
        return false;
    }

    @Override
    public int getContextRefreshedSort() {
        return 31;
    }

    @Override
    public void onContextRefreshed() {
        list = new ArrayList<>();
        if (validator.isEmpty(smtp))
            return;

        JSONArray array = json.toArray(smtp);
        if (validator.isEmpty(array))
            return;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            Map<String, String> map = new HashMap<>();
            map.put("host", object.getString("host"));
            list.add(map);
        }
    }
}
