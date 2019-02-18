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
    public String create(String key, String user, int amount, String currency, String billNo, String notice, String returnUrl) {
        PaypalModel paypal = findByKey(key);
        if (validator.isEmpty(user))
            user = userHelper.id();
        String orderNo = paymentHelper.create(getType(), paypal.getAppId(), user, amount, billNo, notice, null);
        if (validator.isEmpty(orderNo))
            return null;

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.intent("CAPTURE");
        List<PurchaseUnitRequest> purchaseUnits = new ArrayList<>();
        purchaseUnits.add(new PurchaseUnitRequest().amount(new AmountWithBreakdown().currencyCode(currency.toUpperCase())
                .value(numeric.toString(amount * 0.01D, "0.00"))));
        orderRequest.purchaseUnits(purchaseUnits);
        OrdersCreateRequest request = new OrdersCreateRequest().requestBody(orderRequest);
        PayPalHttpClient client = new PayPalHttpClient(new PayPalEnvironment.Live(paypal.getAppId(), paypal.getSecret()));
        try {
            Order order = client.execute(request).result();

            return null;
        } catch (Throwable throwable) {
            logger.warn(throwable, "创建PayPal订单[{}:{}:{}:{}]时发生异常！", paypal.getKey(), paypal.getAppId(), amount, currency);

            return null;
        }
    }

    @Override
    public String getType() {
        return "paypal";
    }

    @Override
    public void resetState(JSONObject object, boolean failureAble) {
    }
}
