package org.lpw.ranch.classify;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.lpw.ranch.model.Recycle;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(ClassifyModel.NAME + ".service")
public class ClassifyServiceImpl implements ClassifyService {
    private static final String CACHE_TREE = ClassifyModel.NAME + ".service.tree:";
    private static final String CACHE_JSON = ClassifyModel.NAME + ".service.json:";

    @Autowired
    protected Cache cache;
    @Autowired
    protected Validator validator;
    @Autowired
    protected Pagination pagination;
    @Autowired
    protected ClassifyDao classifyDao;

    @Override
    public JSONObject query(String code) {
        PageList<ClassifyModel> pl = validator.isEmpty(code) ? classifyDao.query(pagination.getPageSize(), pagination.getPageNum())
                : classifyDao.query(code, pagination.getPageSize(), pagination.getPageNum());
        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(classify -> array.add(getJson(classify.getId(), classify, Recycle.No)));
        object.put("list", array);

        return object;
    }

    @Override
    public JSONObject tree(String code) {
        String cacheKey = CACHE_TREE + code;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {

        }

        return object;
    }

    @Override
    public JSONObject getJsons(String[] ids) {
        JSONObject object = new JSONObject();
        if (validator.isEmpty(ids))
            return object;

        for (String id : ids)
            object.put(id, getJson(id, null, Recycle.No));

        return object;
    }

    @Override
    public JSONObject create(String code, String name, String label) {
        ClassifyModel classify = new ClassifyModel();
        classify.setCode(code);
        classify.setName(name);
        classify.setLabel(label);
        classifyDao.save(classify);

        return getJson(classify.getId(), classify, Recycle.No);
    }

    @Override
    public JSONObject modify(String id, String code, String name, String label) {
        ClassifyModel classify = findById(id);
        if (classify == null)
            return new JSONObject();

        if (!validator.isEmpty(code))
            classify.setCode(code);
        if (!validator.isEmpty(name))
            classify.setName(name);
        if (!validator.isEmpty(label))
            classify.setLabel(label);
        classifyDao.save(classify);

        return getJson(id, classify, Recycle.No);
    }

    protected JSONObject getJson(String id, ClassifyModel classify, Recycle recycle) {
        String cacheKey = CACHE_JSON + id;
        JSONObject object = classify == null ? cache.get(cacheKey) : null;
        if (object == null) {
            object = new JSONObject();
            if (classify == null)
                classify = findById(id);
            if (classify == null || classify.getRecycle() != recycle.getValue()) {
                cache.put(cacheKey, object, false);

                return object;
            }

            object.put("id", classify.getId());
            object.put("code", classify.getCode());
            object.put("name", classify.getName());
            if (!validator.isEmpty(classify.getLabel()))
                object.put("label", classify.getLabel().charAt(0) == '{' ? JSONObject.fromObject(classify.getLabel()) : classify.getLabel());
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    @Override
    public void delete(String id) {
        ClassifyModel classify = findById(id);
        if (classify == null)
            return;

        classify.setRecycle(Recycle.Yes.getValue());
        classifyDao.save(classify);
        cleanCache(classify);
    }

    protected ClassifyModel findById(String id) {
        return classifyDao.findById(id);
    }

    protected void cleanCache(ClassifyModel classify) {
        cache.remove(CACHE_JSON + classify.getId());
        StringBuilder sb = new StringBuilder(CACHE_TREE);
        for (char ch : classify.getCode().toCharArray())
            cache.remove(sb.append(ch).toString());
    }
}
