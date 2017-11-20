package org.lpw.ranch.util;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.meta.helper")
public class MetaHelperImpl implements MetaHelper,ContextRefreshedListener {
    @Inject
    private Io io;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private ModelTables modelTables;
    private Map<String, JSONObject> map;

    @Override
    public JSONObject get(String key) {
        return map.get(key);
    }

    @Override
    public int getContextRefreshedSort() {
        return 11;
    }

    @Override
    public void onContextRefreshed() {
        map = new HashMap<>();
        modelTables.getModelClasses().forEach(modelClass -> {
            try {
                InputStream inputStream = modelClass.getResourceAsStream("meta.json");
                if (inputStream == null)
                    return;

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                io.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
                JSONObject object = json.toObject(outputStream.toString());
                if (object != null)
                    map.put(object.getString("key"), object);
            } catch (Throwable throwable) {
                logger.warn(throwable, "解析Model[{}]元数据时发生异常！", modelClass);
            }
        });
    }
}
