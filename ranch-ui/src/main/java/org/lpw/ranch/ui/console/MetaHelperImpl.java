package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".meta.helper")
public class MetaHelperImpl implements MetaHelper, ContextRefreshedListener {
    @Inject
    private Context context;
    @Inject
    private Io io;
    @Inject
    private Validator validator;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private ModelTables modelTables;
    @Value("${" + ConsoleModel.NAME + ".root:/WEB-INF/ui/}")
    private String root;
    private Map<String, String> map;

    @Override
    public JSONObject get(String domain, String key) {
        return json.toObject(map.computeIfAbsent(domain + ":" + key,
                k -> domain.equals("console") ? "" : map.getOrDefault("console:" + key, "")));
    }

    @Override
    public int getContextRefreshedSort() {
        return 11;
    }

    @Override
    public void onContextRefreshed() {
        map = new ConcurrentHashMap<>();
        modelTables.getModelClasses().forEach(modelClass -> {
            try (InputStream inputStream = modelClass.getResourceAsStream("meta.json");
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                if (inputStream == null)
                    return;

                io.copy(inputStream, outputStream);
                put("console:", outputStream.toString());
            } catch (Throwable throwable) {
                logger.warn(throwable, "解析Model[{}]元数据时发生异常！", modelClass);
            }
        });

        File[] files = new File(context.getAbsolutePath(root)).listFiles();
        if (files != null)
            for (File file : files)
                meta(file);
    }

    private void meta(File file) {
        File[] files = file.listFiles();
        if (files != null)
            for (File f : files)
                if (f.getName().equals("meta") && f.isDirectory())
                    meta(file.getName() + ":", f.listFiles());
    }

    private void meta(String domain, File[] files) {
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                meta(domain, file.listFiles());

                continue;
            }

            if (!file.isFile() || !file.getName().endsWith(".json"))
                continue;

            put(domain, io.readAsString(file.getAbsolutePath()));
        }
    }

    private void put(String domain, String string) {
        JSONObject object = json.toObject(string);
        if (object == null)
            return;

        string = object.toJSONString();
        if (object.containsKey("key"))
            map.put(domain + object.getString("key"), string);
        if (object.containsKey("uri"))
            map.put(domain + object.getString("uri"), string);
    }
}
