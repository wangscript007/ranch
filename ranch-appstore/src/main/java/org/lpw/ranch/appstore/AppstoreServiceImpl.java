package org.lpw.ranch.appstore;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AppstoreModel.NAME + ".service")
public class AppstoreServiceImpl implements AppstoreService {
    @Inject
    private Pagination pagination;
    @Inject
    private AppstoreDao appstoreDao;

    @Override
    public JSONObject query() {
        return appstoreDao.query(pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public AppstoreModel findByProductId(String productId) {
        return appstoreDao.findByProductId(productId);
    }

    @Override
    public void save(String productId, String name, int amount) {
        AppstoreModel appstore = findByProductId(productId);
        if (appstore == null) {
            appstore = new AppstoreModel();
            appstore.setProductId(productId);
        }
        appstore.setName(name);
        appstore.setAmount(amount);
        appstoreDao.save(appstore);
    }

    @Override
    public void delete(String id) {
        appstoreDao.delete(id);
    }
}
