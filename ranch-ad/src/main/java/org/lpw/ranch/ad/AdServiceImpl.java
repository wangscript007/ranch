package org.lpw.ranch.ad;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Service(AdModel.NAME + ".service")
public class AdServiceImpl implements AdService {
    @Inject
    private Cache cache;
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private AdDao adDao;

    @Override
    public JSONObject query(String type, int state) {
        return adDao.query(type, state, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONArray publish(String type) {
        return cache.computeIfAbsent(AdModel.NAME + ":" + type, key -> {
            Set<String> set = new HashSet<>();
            if (!validator.isEmpty(type)) {
                if (type.contains(","))
                    set.addAll(Arrays.asList(type.split(",")));
                else
                    set.add(type);
            }

            return modelHelper.toJson(adDao.query(set, 1).getList());
        }, false);
    }

    @Override
    public void save(AdModel ad) {
        if (validator.isEmpty(ad.getId()) || adDao.findById(ad.getId()) == null)
            ad.setId(null);
        adDao.save(ad);
        cache.remove(AdModel.NAME + ad.getType());
    }

    @Override
    public void delete(String id) {
        AdModel ad = adDao.findById(id);
        adDao.delete(id);
        cache.remove(AdModel.NAME + ad.getType());
    }
}
