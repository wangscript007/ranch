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
    @Value("${" + ResourceModel.NAME + ".auto.sale:false}")
    private boolean autoSale;
    private String random;

    @Override
    public JSONObject query(String type, String name, String label, int state, String uid) {
        return resourceDao.query(type, name, label, state, userHelper.findIdByUid(uid, uid),
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject onsale(String type, String label) {
        int pageSize = pagination.getPageSize(20);
        String cacheKey = getCacheKey("onsale:" + type + ":" + label + ":"
                + pagination.getPageSize(20) + ":" + pagination.getPageNum());
        JSONObject object = cache.get(cacheKey);
        if (object == null)
            cache.put(cacheKey, resourceDao.query(type, null, label, 3, null,
                    pageSize, pagination.getPageNum()).toJson(), false);

        return object;
    }

    @Override
    public ResourceModel findById(String id) {
        return resourceDao.findById(id);
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
        model.setWidth(resource.getWidth());
        model.setHeight(resource.getHeight());
        model.setThumbnail(resource.getThumbnail());
        model.setState(0);
        model.setTime(dateTime.now());
        autoState(model);
        resourceDao.save(model);
        resetRandom();

        return modelHelper.toJson(model);
    }

    @Override
    public JSONObject state(String id, int state) {
        ResourceModel resource = resourceDao.findById(id);
        resource.setState(state);
        autoState(resource);
        resourceDao.save(resource);
        resetRandom();

        return modelHelper.toJson(resource);
    }

    private void autoState(ResourceModel resource) {
        if (autoPass && resource.getState() == 0)
            resource.setState(1);
        if (autoSale && resource.getState() == 1)
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
