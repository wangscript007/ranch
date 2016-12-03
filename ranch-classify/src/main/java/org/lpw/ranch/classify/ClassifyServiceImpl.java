package org.lpw.ranch.classify;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.lpw.ranch.model.Recycle;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Service(ClassifyModel.NAME + ".service")
public class ClassifyServiceImpl implements ClassifyService, DateJob {
    private static final String CACHE_RANDOM = ClassifyModel.NAME + ".service.random";
    private static final String CACHE_TREE = ClassifyModel.NAME + ".service.tree:";
    private static final String CACHE_JSON = ClassifyModel.NAME + ".service.json:";

    @Autowired
    protected Cache cache;
    @Autowired
    protected Validator validator;
    @Autowired
    protected Generator generator;
    @Autowired
    protected Json json;
    @Autowired
    protected Pagination pagination;
    @Autowired
    protected ClassifyDao classifyDao;

    @Override
    public JSONObject query(String code) {
        return toJson(validator.isEmpty(code) ? classifyDao.query(pagination.getPageSize(), pagination.getPageNum())
                : classifyDao.query(code, pagination.getPageSize(), pagination.getPageNum()), Recycle.No);
    }

    @Override
    public JSONArray tree(String code) {
        String cacheKey = CACHE_TREE + getRandom() + code;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = new JSONArray();
            for (ClassifyModel classify : classifyDao.query(code, 0, 0).getList()) {
                JSONObject object = getJson(classify.getId(), classify, Recycle.No);
                JSONObject parent = findParent(array, classify.getCode());
                if (parent == null)
                    array.add(object);
                else
                    json.addAsArray(parent, "children", object);
            }
            cache.put(cacheKey, array, false);
        }

        return array;
    }

    protected JSONObject findParent(JSONArray array, String code) {
        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String string = object.getString("code");
            if (code.startsWith(string) && !code.equals(string)) {
                if (object.has("children")) {
                    JSONObject child = findParent(object.getJSONArray("children"), code);
                    if (child != null)
                        return child;
                }

                return object;
            }
        }

        return null;
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
        String cacheKey = CACHE_JSON + getRandom() + id;
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

        classifyDao.delete(classify.getCode());
        resetRandom();
    }

    @Override
    public JSONObject recycle() {
        return toJson(classifyDao.recycle(pagination.getPageSize(), pagination.getPageNum()), Recycle.Yes);
    }

    protected JSONObject toJson(PageList<ClassifyModel> pl, Recycle recycle) {
        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(classify -> array.add(getJson(classify.getId(), classify, recycle)));
        object.put("list", array);

        return object;
    }

    @Override
    public void restore(String id) {
        ClassifyModel classify = findById(id);
        if (classify == null)
            return;

        Set<String> codes = new HashSet<>();
        StringBuilder code = new StringBuilder();
        for (char ch : classify.getCode().toCharArray())
            codes.add(code.append(ch).toString());
        classifyDao.restore(codes);
        resetRandom();
    }

    @Override
    public void refresh() {
        resetRandom();
    }

    protected ClassifyModel findById(String id) {
        return classifyDao.findById(id);
    }

    protected String getRandom() {
        String random = cache.get(CACHE_RANDOM);

        return validator.isEmpty(random) ? resetRandom() : random;
    }

    protected String resetRandom() {
        String random = generator.random(32);
        cache.put(CACHE_RANDOM, random, true);

        return random;
    }

    @Override
    public void executeDateJob() {
        resetRandom();
    }
}
