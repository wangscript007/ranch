package org.lpw.ranch.paypal.transaction;

import com.alibaba.fastjson.JSONObject;
import com.braintreepayments.http.serializer.Json;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.Money;
import com.paypal.orders.Order;
import com.paypal.orders.OrdersGetRequest;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.paypal.PaypalModel;
import org.lpw.ranch.paypal.PaypalService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * https://developer.paypal.com/docs/checkout/integrate/#6-verify-the-transaction
 *
 * @author lpw
 */
@Service(TransactionModel.NAME + ".service")
public class TransactionServiceImpl implements TransactionService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Numeric numeric;
    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private PaypalService paypalService;
    @Inject
    private TransactionDao transactionDao;

    @Override
    public JSONObject query(String user, String[] creates) {
        return transactionDao.query(user, dateTime.toTimeRange(creates), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public String verify(String key, String tradeNo) {
        PaypalModel paypal = paypalService.findByKey(key);
        try {
            Order order = new PayPalHttpClient(new PayPalEnvironment.Live(paypal.getAppId(), paypal.getSecret()))
                    .execute(new OrdersGetRequest(tradeNo)).result();
            if (logger.isInfoEnable())
                logger.info("校验PayPal[{}]支付[{}]结果[{}]。", key, tradeNo, order.status());
            if (notSuccess(order.status()))
                return null;

            String result = new Json().serialize(order);
            if (logger.isInfoEnable())
                logger.info("校验PayPal[{}]支付[{}]结果[{}]。", key, tradeNo, result);
            order.purchaseUnits().forEach(purchaseUnit -> {
                if (purchaseUnit == null)
                    return;

                if (purchaseUnit.payments() == null || validator.isEmpty(purchaseUnit.payments().captures())) {
//                    create(paypal, order.id(), purchaseUnit.amount().currencyCode(), purchaseUnit.amount().value(), result,
//                            order.createTime(), order.updateTime());

                    return;
                }

                purchaseUnit.payments().captures().forEach(capture -> {
                    if (capture == null || notSuccess(capture.status()) || transactionDao.count(capture.id()) > 0)
                        return;

                    create(paypal, capture.id(), capture.amount().currencyCode(), capture.amount().value(), result,
                            capture.createTime(), capture.updateTime());
                });
            });

            return result;
        } catch (Throwable throwable) {
            logger.warn(throwable, "获取PayPal交易[{}:{}]信息发生异常！", key, tradeNo);

            return null;
        }
    }

    private boolean notSuccess(String status) {
        return !status.equals("APPROVED") && !status.equals("COMPLETED");
    }

    private void create(PaypalModel paypal, String tradeNo, String currency, String amount, String response, String createTime, String updateTime) {
        TransactionModel transaction = new TransactionModel();
        transaction.setKey(paypal.getKey());
        transaction.setUser(userHelper.id());
        transaction.setAmount(numeric.toInt(numeric.toDouble(amount) * 100));
        transaction.setOrderNo(paymentHelper.create("paypal", paypal.getAppId(), transaction.getUser(), transaction.getAmount(),
                "", null, null));
        transaction.setTradeNo(tradeNo);
        transaction.setCurrency(currency);
        transaction.setResponse(response);
        transaction.setCreate(dateTime.toTime(createTime, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
        transaction.setFinish(dateTime.toTime(updateTime, "yyyy-MM-dd'T'HH:mm:ss'Z'"));
        transactionDao.save(transaction);
        paymentHelper.complete(transaction.getOrderNo(), transaction.getAmount(), transaction.getTradeNo(), 1, null);
    }
}
