package org.lpw.ranch.editor.resource;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ResourceModel.NAME + ".service")
public class ResourceServiceImpl implements ResourceService {
    private static final String CACHE_KEY = ResourceModel.NAME + ".service.cache:";

    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private Cache cache;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private ResourceDao resourceDao;
    @Value("${" + ResourceModel.NAME + ".auto.pass:false}")
    private boolean autoPass;
    @Value("${" + ResourceModel.NAME + ".auto.onsale:false}")
    private boolean autoOnsale;
    private String random;

    @Override
    public JSONObject query(String type, String name, int state, String uid) {
        return resourceDao.query(type, name, state, userHelper.findIdByUid(uid, uid),
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject onsale(String type) {
        int pageSize = pagination.getPageSize(20);
        String cacheKey = getCacheKey("onsale:" + type + ":" + pagination.getPageSize(20) + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, resourceDao.query(type, null, 3, null, pageSize, pagination.getPageNum()).toJson(),
                    false);

        return object;
    }

    @Override
    public JSONObject save(ResourceModel resource) {
        ResourceModel model = validator.isEmpty(resource.getId()) ? null : resourceDao.findById(resource.getId());
        if (model == null) {
            model = new ResourceModel();
            model.setUser(userHelper.id());
        }
        model.setType(resource.getType());
        model.setSort(resource.getSort());
        model.setName(resource.getName());
        model.setLabel(resource.getLabel());
        model.setUri(resource.getUri());
        model.setState(0);
        model.setTime(dateTime.now());
        setState(model);
        resourceDao.save(model);
        resetRandom();

        return modelHelper.toJson(model);
    }

    private void setState(ResourceModel resource) {
        if (autoPass && resource.getState() == 0)
            resource.setState(1);
        if (autoOnsale && resource.getState() == 1)
            resource.setState(3);
    }

    @Override
    public void delete(String id) {
        resourceDao.delete(id);
        resetRandom();
    }

    private String getCacheKey(String key) {
        if (random == null)
            resetRandom();

        return CACHE_KEY + random + key;
    }

    private void resetRandom() {
        random = generator.random(32) + ":";
    }
}
