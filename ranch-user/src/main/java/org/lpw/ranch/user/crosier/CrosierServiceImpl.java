package org.lpw.ranch.user.crosier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;
import org.lpw.ranch.user.UserService;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.storage.StorageListener;
import org.lpw.tephra.storage.Storages;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(CrosierModel.NAME + ".service")
public class CrosierServiceImpl implements CrosierService, StorageListener {
    @Inject
    private Context context;
    @Inject
    private Message message;
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private Json json;
    @Inject
    private Numeric numeric;
    @Inject
    private Cache cache;
    @Inject
    private Logger logger;
    @Inject
    private UserService userService;
    @Inject
    private CrosierDao crosierDao;
    @Value("${" + CrosierModel.NAME + ".permit:/WEB-INF/permit}")
    private String permit;
    private int[] grades = {0, 90};
    private Map<String, Integer> permits = new HashMap<>();
    private Map<Integer, Map<String, Set<Map<String, String>>>> map = new ConcurrentHashMap<>();

    @Override
    public JSONArray grades() {
        return cache.computeIfAbsent(CrosierModel.NAME + context.getLocale().toString(), key -> {
            JSONArray grades = new JSONArray();
            for (int n : this.grades) {
                JSONObject grade = new JSONObject();
                grade.put("grade", n);
                grade.put("name", message.get(CrosierModel.NAME + ".grade." + n));
                grades.add(grade);
            }

            return grades;
        }, false);
    }

    @Override
    public JSONArray pathes(int grade) {
        JSONArray pathes = new JSONArray();
        crosierDao.query(grade).getList().forEach(crosier -> pathes.add(crosier.getPath()));

        return pathes;
    }

    @Override
    public void save(int grade, String pathes) {
        crosierDao.delete(grade);
        if (validator.isEmpty(pathes))
            return;

        for (String path : converter.toArray(pathes, ",,")) {
            String[] array = converter.toArray(path, ";");
            String[] uriParameter = uriParameter(array[array.length - 1]);
            if (!uriParameter[0].startsWith("/")) {
                String[] parentUriParameter = uriParameter(array[array.length - 2]);
                uriParameter[0] = parentUriParameter[0].substring(0, parentUriParameter[0].lastIndexOf('/') + 1) + uriParameter[0];
            }
            CrosierModel crosier = new CrosierModel();
            crosier.setGrade(grade);
            crosier.setUri(uriParameter[0]);
            crosier.setParameter(uriParameter[1]);
            crosier.setPath(path);
            crosierDao.save(crosier);
            load(grade);
        }
    }

    private String[] uriParameter(String path) {
        int index = path.indexOf('{');
        if (index == -1)
            return new String[]{path, ""};

        return new String[]{path.substring(0, index), path.substring(index)};
    }

    @Override
    public boolean permit(String uri, Map<String, String> parameter) {
        if (map.isEmpty())
            for (int grade : grades)
                load(grade);

        UserModel user = userService.fromSession();
        if (permits.containsKey(uri)) {
            int grade = permits.get(uri);

            return grade == -1 || (user != null && user.getGrade() >= grade);
        }

        if (user == null)
            return false;

        if (user.getCode().equals("99999999"))
            return true;

        if (!map.containsKey(user.getGrade()))
            return false;

        Map<String, Set<Map<String, String>>> map = this.map.get(user.getGrade());
        if (!map.containsKey(uri))
            return false;

        Set<Map<String, String>> set = map.get(uri);
        if (set.isEmpty())
            return true;

        for (Map<String, String> param : set) {
            int count = 0;
            for (String key : param.keySet())
                if (parameter.containsKey(key) && parameter.get(key).equals(param.get(key)))
                    count++;
            if (count == param.size())
                return true;
        }

        return false;
    }

    private void load(int grade) {
        Map<String, Set<Map<String, String>>> map = new HashMap<>();
        crosierDao.query(grade).getList().forEach(crosier -> {
            for (String path : converter.toArray(crosier.getPath(), ";")) {
                String[] uriParameter = uriParameter(path);
                Set<Map<String, String>> set = map.computeIfAbsent(uriParameter[0], key -> new HashSet<>());
                if (!validator.isEmpty(uriParameter[1])) {
                    set.add(json.toMap(json.toObject(uriParameter[1])));
                }
                map.put(uriParameter[0], set);
            }
        });
        this.map.put(grade, map);
        System.out.println("#####################################################");
        System.out.println(this.map);
        System.out.println("#####################################################");
    }

    @Override
    public String getStorageType() {
        return Storages.TYPE_DISK;
    }

    @Override
    public String[] getScanPathes() {
        return new String[]{permit};
    }

    @Override
    public void onStorageChanged(String path, String absolutePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(absolutePath))) {
            Map<String, Integer> map = new HashMap<>();
            for (String line; (line = reader.readLine()) != null; ) {
                line = line.trim();
                if (line.charAt(0) == '#')
                    continue;

                int index = line.indexOf(':');
                if (index == -1)
                    map.put(line, -1);
                else {
                    map.put(line.substring(0, index), numeric.toInt(line.substring(index + 1)));
                }
            }
            permits = map;
            if (logger.isInfoEnable())
                logger.info("更新免校验权限配置[{}]。", permits);
        } catch (Throwable throwable) {
            logger.warn(throwable, "读取权限配置[{}:{}]文件失败！", path, absolutePath);
        }
    }
}
