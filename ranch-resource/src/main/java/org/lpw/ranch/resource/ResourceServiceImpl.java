package org.lpw.ranch.resource;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ResourceModel.NAME + ".service")
public class ResourceServiceImpl implements ResourceService {
    private static final String CACHE_KEY = ResourceModel.NAME + ".service.cache:";

    @Inject
    private Context context;
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private Cache cache;
    @Inject
    private Http http;
    @Inject
    private Io io;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private AsyncService asyncService;
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
            cache.put(cacheKey, object = resourceDao.query(type, null, label, 3, null,
                    pageSize, pagination.getPageNum()).toJson(), false);

        return object;
    }

    @Override
    public ResourceModel findById(String id) {
        return resourceDao.findById(id);
    }

    @Override
    public JSONObject save(ResourceModel resource, String download) {
        ResourceModel model = validator.isEmpty(resource.getId()) ? null : resourceDao.findById(resource.getId());
        if (model == null) {
            model = new ResourceModel();
            model.setUser(userHelper.id());
        }
        model.setType(validator.isEmpty(resource.getType()) ? "" : resource.getType());
        model.setSort(resource.getSort());
        model.setName(validator.isEmpty(resource.getName()) ? "" : resource.getName());
        model.setLabel(resource.getLabel());
        if (validator.isEmpty(download)) {
            model.setUri(resource.getUri());
            model.setSize(resource.getSize());
        } else
            download(model, download);
        model.setWidth(resource.getWidth());
        model.setHeight(resource.getHeight());
        model.setThumbnail(resource.getThumbnail());
        model.setAuthor(resource.getAuthor());
        model.setState(0);
        model.setTime(dateTime.now());
        autoState(model);
        resourceDao.save(model);
        resetRandom();

        return modelHelper.toJson(model);
    }

    private void download(ResourceModel resource, String url) {
        File file = new File(context.getAbsoluteRoot() + "/" + generator.random(32));
        Map<String, String> header = new HashMap<>();
        try (OutputStream outputStream = new FileOutputStream(file)) {
            http.post(url, null, null, header, outputStream);
        } catch (Throwable throwable) {
            io.delete(file);
            logger.warn(throwable, "下载资源[{}]时发生异常！", url);

            return;
        }

        String contentType = header.get("content-type");
        int indexOf;
        if (!validator.isEmpty(contentType) && (indexOf = contentType.lastIndexOf('/')) > -1)
            file = io.move(file, new File(file.getAbsolutePath() + "." + contentType.substring(indexOf + 1)));
        resource.setUri(contentType.startsWith("image/") ? wormholeHelper.image(null, null, null, file)
                : wormholeHelper.file(null, null, null, file));
        resource.setSize(file.length());
        io.delete(file);
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
