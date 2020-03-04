package org.lpw.ranch.user.crosier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(CrosierModel.NAME + ".service")
public class CrosierServiceImpl implements CrosierService {
    @Inject
    private Context context;
    @Inject
    private Message message;
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private Cache cache;
    @Inject
    private CrosierDao crosierDao;

    @Override
    public JSONArray grades() {
        return cache.computeIfAbsent(CrosierModel.NAME + context.getLocale().toString(), key -> {
            JSONArray grades = new JSONArray();
            for (int n : new int[]{0, 90}) {
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
            CrosierModel crosier = new CrosierModel();
            crosier.setGrade(grade);
            crosier.setUri("");
            crosier.setPath(path);
            crosierDao.save(crosier);
        }
    }
}
