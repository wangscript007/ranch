package org.lpw.ranch.paypal.transaction;

import com.alibaba.fastjson.JSONObject;
import com.braintreepayments.http.serializer.Json;
import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
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
            if (notSuccess(order.status()))
                return null;

            String result = new Json().serialize(order);
            order.purchaseUnits().forEach(purchaseUnit -> purchaseUnit.payments().captures().forEach(capture -> {
                if (notSuccess(capture.status()) || transactionDao.count(capture.id()) > 0)
                    return;

                TransactionModel transaction = new TransactionModel();
                transaction.setKey(key);
                transaction.setUser(userHelper.id());
                transaction.setAmount(numeric.toInt(numeric.toDouble(capture.amount().value()) * 100));
                transaction.setOrderNo(paymentHelper.create("paypal", paypal.getAppId(), transaction.getUser(), transaction.getAmount(),
                        "", null, null));
                transaction.setTradeNo(capture.id());
                transaction.setCurrency(capture.amount().currencyCode());
                transaction.setResponse(result);
                transaction.setCreate(dateTime.toTime(capture.createTime(), "yyyy-MM-dd'T'HH:mm:ss'Z'"));
                transaction.setFinish(dateTime.toTime(capture.updateTime(), "yyyy-MM-dd'T'HH:mm:ss'Z'"));
                transactionDao.save(transaction);
                paymentHelper.complete(transaction.getOrderNo(), transaction.getAmount(), transaction.getTradeNo(), 1, null);
            }));

            return result;
        } catch (Throwable throwable) {
            logger.warn(throwable, "获取PayPal交易[{}:{}]信息发生异常！", key, tradeNo);

            return null;
        }
    }

    private boolean notSuccess(String status) {
        return !status.equals("APPROVED") && !status.equals("COMPLETED");
    }
}
