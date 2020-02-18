package org.lpw.ranch.ui.console;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.Model;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(ConsoleModel.NAME + ".meta.helper")
public class MetaHelperImpl implements MetaHelper, ContextRefreshedListener {
    @Inject
    private Cache cache;
    @Inject
    private Context context;
    @Inject
    private Message message;
    @Inject
    private Io io;
    @Inject
    private Validator validator;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private Set<Model> models;
    @Value("${" + ConsoleModel.NAME + ".root:/WEB-INF/ui/}")
    private String root;
    private Map<String, String> map;

    @Override
    public JSONObject get(String domain, String key) {
        return cache.computeIfAbsent(ConsoleModel.NAME + ".meta:" + domain + ":" + key + ":" + context.getLocale().toString(), k -> {
            JSONObject meta = json.toObject(map.computeIfAbsent(domain + ":" + key, mk -> domain.equals("console") ? "" : map.getOrDefault("console:" + key, "")));
            if (meta == null)
                return new JSONObject();

            String prefix = meta.getString("key");
            setLabel(prefix, meta, "props", "name");
            for (String mk : meta.keySet()) {
                if (mk.equals("key") || mk.equals("uri") || mk.equals("props"))
                    continue;

                JSONObject object = meta.getJSONObject(mk);
                if (object.containsKey("search"))
                    setLabel(prefix, object, "search", "name");
                if (object.containsKey("ops"))
                    setLabel(ConsoleModel.NAME + ".op", object, "ops", "service");
                if (object.containsKey("toolbar"))
                    setLabel(ConsoleModel.NAME + ".op", object, "toolbar", "service");
            }

            return meta;
        }, false);
    }

    private void setLabel(String prefix, JSONObject object, String key, String k) {
        if (!object.containsKey(key))
            return;

        JSONArray array = object.getJSONArray(key);
        for (int i = 0, size = array.size(); i < size; i++)
            setLabel(prefix, array.getJSONObject(i), k);
    }

    private void setLabel(String[] prefix, JSONObject object, String[] key) {
        String label = null;
        if (object.containsKey("label")) {
            label = object.getString("label");
            if (label.charAt(0) == '.')
                label = prefix + label;
        } else if (object.containsKey(key))
            label = prefix + "." + object.getString(key);
        if (label != null)
            object.put("label", message.get(label));

        if (object.containsKey("labels")) {
            String labels = object.getString("labels");
            if (labels.charAt(0) == '.')
                labels = prefix + labels;
            JSONArray array = new JSONArray();
            array.addAll(Arrays.asList(message.getAsArray(labels)));
            object.put("labels", array);
        } else if (object.containsKey("values")) {
            JSONObject values = object.getJSONObject("values");
            for (String k : values.keySet()) {
                String v = values.getString(k);
                if (v.charAt(0) == '.')
                    v = label + v;
                values.put(k, message.get(v));
            }
        }

        for(String p:prefix){
            for (String k:key){
                if(object.containsKey(k)){
                    String value=object.getString(k);
                    if(value.charAt(0)=='.')
                }
            }
        }
    }

    @Override
    public int getContextRefreshedSort() {
        return 11;
    }

    @Override
    public void onContextRefreshed() {
        map = new ConcurrentHashMap<>();
        models.forEach(model -> {
            Class<? extends Model> modelClass = model.getClass();
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

        String prefix = object.getString("key") + ".";
        JSONArray props = object.getJSONArray("props");
        for (int i = 0, size = props.size(); i < size; i++) {
            JSONObject prop = props.getJSONObject(i);
            if (json.has(prop, "type", "image") && !prop.containsKey("upload"))
                prop.put("upload", prefix + prop.getString("name"));
        }

        string = object.toJSONString();
        if (object.containsKey("key"))
            map.put(domain + object.getString("key"), string);
        if (object.containsKey("uri"))
            map.put(domain + object.getString("uri"), string);
    }
}
