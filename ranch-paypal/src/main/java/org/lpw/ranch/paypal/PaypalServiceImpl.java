package org.lpw.ranch.paypal;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.payment.helper.PaymentListener;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Service(PaypalModel.NAME + ".service")
public class PaypalServiceImpl implements PaypalService, PaymentListener {
    @Inject
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private PaypalDao paypalDao;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(paypalDao.query().getList());
    }

    @Override
    public PaypalModel findByKey(String key) {
        return paypalDao.findByKey(key);
    }

    @Override
    public PaypalModel findByAppId(String appId) {
        return paypalDao.findByAppId(appId);
    }

    @Override
    public JSONObject save(PaypalModel paypal) {
        PaypalModel model = paypalDao.findByKey(paypal.getKey());
        if (model == null) {
            model = new PaypalModel();
            model.setKey(paypal.getKey());
        }
        model.setName(paypal.getName());
        model.setAppId(paypal.getAppId());
        model.setSecret(paypal.getSecret());
        paypalDao.save(model);

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String id) {
        paypalDao.delete(id);
    }

    @Override
    public String getType() {
        return "paypal";
    }

    @Override
    public void resetState(JSONObject object, boolean failureAble) {
    }
}
