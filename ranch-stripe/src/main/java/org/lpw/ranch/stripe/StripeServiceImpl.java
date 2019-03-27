package org.lpw.ranch.stripe;

import com.alibaba.fastjson.JSONArray;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(StripeModel.NAME + ".service")
public class StripeServiceImpl implements StripeService {
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private StripeDao stripeDao;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(stripeDao.query().getList());
    }

    @Override
    public StripeModel findByKey(String key) {
        return stripeDao.findByKey(key);
    }

    @Override
    public void save(StripeModel stripe) {
        StripeModel model = stripeDao.findByKey(stripe.getKey());
        stripe.setId(model == null ? null : model.getId());
        stripeDao.save(stripe);
    }

    @Override
    public void delete(String id) {
        stripeDao.delete(id);
    }
}
