package org.lpw.ranch.classify;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.ranch.recycle.RecycleHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ClassifyModel.NAME + ".service")
public class ClassifyServiceImpl implements ClassifyService, DateJob {
    private static final String CACHE_RANDOM = ClassifyModel.NAME + ".service.random";
    private static final String CACHE_TREE = ClassifyModel.NAME + ".service.tree:";
    private static final String CACHE_JSON = ClassifyModel.NAME + ".service.json:";
    private static final String CACHE_GET = ClassifyModel.NAME + ".service.get:";
    private static final String CACHE_LIST = ClassifyModel.NAME + ".service.list:";

    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private Generator generator;
    @Inject
    private Json json;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private RecycleHelper recycleHelper;
    @Inject
    private ClassifyDao classifyDao;

    @Override
    public JSONObject query(String code, String key, String value, String name) {
        return toJson(validator.isEmpty(code) ? classifyDao.query(pagination.getPageSize(20), pagination.getPageNum())
                : classifyDao.query(code, key, value, name, pagination.getPageSize(20), pagination.getPageNum()));
    }

    @Override
    public JSONArray tree(String code) {
        String cacheKey = CACHE_TREE + getRandom() + code;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = new JSONArray();
            for (ClassifyModel classify : classifyDao.query(code, null, null, null, 0, 0).getList()) {
                JSONObject object = toJson(classify.getId(), classify, Recycle.No);
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

    private JSONObject findParent(JSONArray array, String code) {
        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String string = object.getString("code");
            if (code.startsWith(string) && !code.equals(string)) {
                if (object.containsKey("children")) {
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
    public JSONObject get(String[] ids) {
        String cacheKey = CACHE_GET + converter.toString(ids);
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            object = new JSONObject();
            for (String id : ids) {
                JSONObject json = toJson(id, null, Recycle.No);
                if (json.isEmpty())
                    continue;

                object.put(id, json);
                if (!json.containsKey("links"))
                    continue;

                Object links = json.get("links");
                if (links instanceof JSONArray)
                    object.putAll(get(((JSONArray) links).toArray(new String[0])));
            }
            cache.put(cacheKey, object, false);
        }

        return object;
    }

    @Override
    public JSONObject find(String code, String key) {
        String cacheKey = CACHE_GET + getRandom() + code + key;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            ClassifyModel classify = classifyDao.findByCodeKey(code, key);
            if (classify == null)
                return new JSONObject();

            cache.put(cacheKey, object = toJson(classify.getId(), classify, Recycle.No), false);
        }

        return object;
    }

    @Override
    public JSONArray list(String code, String key, String name) {
        String cacheKey = CACHE_LIST + getRandom() + code + key + name;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = new JSONArray();
            for (ClassifyModel classify : classifyDao.query(code, null, null, null, 0, 0).getList())
                if (contains(classify, key, name))
                    array.add(toJson(classify.getId(), classify, Recycle.No));
            cache.put(cacheKey, array, false);
        }

        return array;
    }

    private boolean contains(ClassifyModel classify, String key, String name) {
        boolean[] empty = new boolean[]{validator.isEmpty(key), validator.isEmpty(name), validator.isEmpty(classify.getKey())};
        if (empty[0] && empty[1])
            return true;

        if (empty[0])
            return classify.getName().contains(name);

        if (empty[2])
            return false;

        if (empty[1])
            return classify.getKey().contains(key);

        return classify.getKey().contains(key) && classify.getName().contains(name);
    }

    @Override
    public JSONObject save(ClassifyModel classify) {
        ClassifyModel model = save(classify.getCode(), classify.getKey(), classify.getValue(), classify.getName(), classify.getJson());
        resetRandom();

        return toJson(model.getId(), model, Recycle.No);
    }

    private JSONObject toJson(String id, ClassifyModel classify, Recycle recycle) {
        String cacheKey = CACHE_JSON + getRandom() + id;
        JSONObject object = classify == null ? cache.get(cacheKey) : null;
        if (object == null) {
            object = new JSONObject();
            if (classify == null)
                classify = findById(id);
            if (classify == null || classify.getRecycle() != recycle.getValue())
                cache.put(cacheKey, object, false);
            else
                cache.put(cacheKey, object = modelHelper.toJson(classify), false);
        }

        return object;
    }

    @Override
    public void saves(JSONArray array) {
        if (validator.isEmpty(array))
            return;

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String code = object.getString("code");
            String key = object.getString("key");
            String name = object.getString("name");
            if (nonEmptyMaxLength(code) && nonEmptyMaxLength(key) && nonEmptyMaxLength(name))
                save(code, key, object.getString("value"), name, null);
        }
        resetRandom();
    }

    private boolean nonEmptyMaxLength(String string) {
        return !validator.isEmpty(string) && string.length() <= 100;
    }

    private ClassifyModel save(String code, String key, String value, String name, String json) {
        ClassifyModel classify = classifyDao.findByCodeKey(code, key);
        if (classify == null) {
            classify = new ClassifyModel();
            classify.setCode(code);
            classify.setKey(key);
        }
        classify.setValue(value);
        classify.setName(name);
        classify.setJson(json);
        classifyDao.save(classify);

        return classify;
    }

    @Override
    public void delete(String id) {
        recycleHelper.delete(ClassifyModel.class, id);
        resetRandom();
    }

    @Override
    public JSONObject recycle() {
        return recycleHelper.recycle(ClassifyModel.class);
    }

    private JSONObject toJson(PageList<ClassifyModel> pl) {
        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(classify -> array.add(toJson(classify.getId(), classify, Recycle.No)));
        object.put("list", array);

        return object;
    }

    @Override
    public void restore(String id) {
        recycleHelper.restore(ClassifyModel.class, id);
        resetRandom();
    }

    @Override
    public void refresh() {
        resetRandom();
    }

    @Override
    public ClassifyModel findById(String id) {
        return classifyDao.findById(id);
    }

    private String getRandom() {
        String random = cache.get(CACHE_RANDOM);

        return validator.isEmpty(random) ? resetRandom() : random;
    }

    private String resetRandom() {
        String random = generator.random(32);
        cache.put(CACHE_RANDOM, random, true);

        return random;
    }

    @Override
    public void executeDateJob() {
        resetRandom();
    }
}
