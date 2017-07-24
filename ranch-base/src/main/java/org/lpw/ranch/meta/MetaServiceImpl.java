package org.lpw.ranch.meta;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.ctrl.execute.Execute;
import org.lpw.tephra.ctrl.execute.ExecuteListener;
import org.lpw.tephra.ctrl.execute.Executor;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Message;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(MetaModel.NAME + ".service")
public class MetaServiceImpl implements MetaService, ExecuteListener {
    @Inject
    private Io io;
    @Inject
    private Json json;
    @Inject
    private Message message;
    @Inject
    private Logger logger;
    private Map<String, JSONObject> map = new HashMap<>();

    @Override
    public JSONObject get(String service) {
        return map.get(service);
    }

    @Override
    public void definition(Execute classExecute, Execute methodExecute, Executor executor) {
        InputStream inputStream = executor.getBean().getClass().getResourceAsStream(methodExecute.name() + ".json");
        if (inputStream == null)
            return;

        String key = classExecute == null ? methodExecute.name() : (classExecute.key() + "." + methodExecute.name());
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            io.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
            JSONObject meta = json.toObject(outputStream.toString());
            JSONArray cols = meta.getJSONArray("cols");
            for (int i = 0, size = cols.size(); i < size; i++) {
                JSONObject col = cols.getJSONObject(i);
                if (col.containsKey("label"))
                    continue;

                col.put("label", message.get(classExecute.key() + "." + col.getString("name")));
            }
            map.put(key, meta);
        } catch (Throwable e) {
            logger.warn(e, "读取META[{}]时发生异常！", key);
        }
    }
}
