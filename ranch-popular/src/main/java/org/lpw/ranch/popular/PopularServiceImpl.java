package org.lpw.ranch.popular;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.TimeUnit;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(PopularModel.NAME + ".service")
public class PopularServiceImpl implements PopularService, MinuteJob {
    private static final String CACHE_PUBLISH = PopularModel.NAME + ".publish:";

    @Inject
    private Cache cache;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private PopularDao popularDao;
    private List<String[]> list = Collections.synchronizedList(new ArrayList<>());

    @Override
    public JSONObject query(String key) {
        return popularDao.query(key, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONArray publish(String key, int size) {
        String cacheKey = CACHE_PUBLISH + key + ":" + size + ":" + System.currentTimeMillis() / TimeUnit.Hour.getTime();
        JSONArray array = cache.get(cacheKey);
        if (array == null)
            cache.put(cacheKey, array = modelHelper.toJson(popularDao.query(key, size, 0).getList()), false);

        return array;
    }

    @Override
    public void increase(String key, String value) {
        list.add(new String[]{key, value});
    }

    @Override
    public void delete(String id) {
        popularDao.delete(id);
    }

    @Override
    public void executeMinuteJob() {
        if (list.isEmpty())
            return;

        Map<String, PopularModel> map = new HashMap<>();
        while (!list.isEmpty()) {
            String[] array = list.remove(0);
            String key = array[0].hashCode() + ":" + array[1].hashCode();
            PopularModel popular;
            if (map.containsKey(key))
                popular = map.get(key);
            else {
                popular = popularDao.find(array[0], array[1]);
                if (popular == null) {
                    popular = new PopularModel();
                    popular.setKey(array[0]);
                    popular.setValue(array[1]);
                }
                map.put(key, popular);
            }
            popular.setCount(popular.getCount() + 1);
        }
        map.values().forEach(popularDao::save);
    }
}
