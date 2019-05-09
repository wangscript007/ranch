package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(LinkModel.NAME + ".service")
public class LinkServiceImpl implements LinkService {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private DateTime dateTime;
    @Inject
    private Json json;
    @Inject
    private Generator generator;
    @Inject
    private Cache cache;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private LinkDao linkDao;
    private Map<String, String> random = new ConcurrentHashMap<>();

    @Override
    public JSONObject query(String type, String id1, String id2) {
        return cache.computeIfAbsent(getCacheKey(type, "query", id1 + ":" + id2), key -> {
            if (!validator.isEmpty(id1))
                return id1.contains(",") ? linkDao.query1(type, toSet(id1), pagination.getPageSize(20), pagination.getPageNum()).toJson()
                        : linkDao.query1(type, id1, pagination.getPageSize(20), pagination.getPageNum()).toJson();

            if (!validator.isEmpty(id2))
                return id2.contains(",") ? linkDao.query2(type, toSet(id2), pagination.getPageSize(20), pagination.getPageNum()).toJson()
                        : linkDao.query2(type, id2, pagination.getPageSize(20), pagination.getPageNum()).toJson();

            return new JSONObject();
        }, false);
    }

    @Override
    public JSONObject count(String type, String id1, String id2) {
        return cache.computeIfAbsent(getCacheKey(type, "count", id1 + ":" + id2), key -> {
            Set<String> ids;
            if (!validator.isEmpty(id1))
                return id1.contains(",") ? count(ids = toSet(id1), linkDao.count1(type, ids))
                        : count(id1, linkDao.count(type, id1, null));

            if (!validator.isEmpty(id2))
                return id2.contains(",") ? count(ids = toSet(id2), linkDao.count2(type, ids))
                        : count(id2, linkDao.count(type, null, id2));

            return new JSONObject();
        }, false);
    }

    private Set<String> toSet(String string) {
        return new HashSet<>(Arrays.asList(converter.toArray(string, ",")));
    }

    private JSONObject count(Set<String> ids, Map<String, Integer> map) {
        JSONObject object = new JSONObject();
        ids.forEach(id -> object.put(id, map.getOrDefault(id, 0)));

        return object;
    }

    private JSONObject count(String id, int count) {
        JSONObject object = new JSONObject();
        object.put(id, count);

        return object;
    }

    @Override
    public JSONArray exists(String type, String[] id1s, String[] id2s) {
        if (validator.isEmpty(id1s) || validator.isEmpty(id2s))
            return new JSONArray();

        return cache.computeIfAbsent(getCacheKey(type, "exists", Arrays.toString(id1s) + ":" + Arrays.toString(id2s)), key -> {
            JSONArray array = new JSONArray();
            for (String id1 : id1s) {
                for (String id2 : id2s) {
                    JSONObject object = new JSONObject();
                    object.put("id1", id1);
                    object.put("id2", id2);
                    object.put("exists", linkDao.count(type, id1, id2) == 1);
                    array.add(object);
                }
            }

            return array;
        }, false);
    }

    @Override
    public JSONObject find(String type, String id1, String id2) {
        return cache.computeIfAbsent(getCacheKey(type, "find", id1 + ":" + id2), key -> {
            LinkModel link = linkDao.find(type, id1, id2);

            return link == null ? new JSONObject() : modelHelper.toJson(link);
        }, false);
    }

    @Override
    public JSONObject save(LinkModel link) {
        LinkModel model = linkDao.find(link.getType(), link.getId1(), link.getId2());
        if (model == null) {
            model = new LinkModel();
            model.setType(link.getType());
            model.setId1(link.getId1());
            model.setId2(link.getId2());
        }
        model.setJson(link.getJson());
        model.setTime(dateTime.now());
        linkDao.save(model);
        removeCacheKey(link.getType());

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String type, String id1, String id2) {
        linkDao.delete(type, id1, id2);
        removeCacheKey(type);
    }

    private String getCacheKey(String type, String operation, String key) {
        return LinkModel.NAME + ":" + type + ":" + random.computeIfAbsent(type, k -> generator.random(32)) + ":" + operation + ":" + key;
    }

    private void removeCacheKey(String type) {
        random.remove(type);
    }
}
