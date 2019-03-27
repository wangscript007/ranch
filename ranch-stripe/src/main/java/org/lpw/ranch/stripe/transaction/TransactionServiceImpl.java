package org.lpw.ranch.stripe.transaction;

import com.alibaba.fastjson.JSONObject;
import com.stripe.Stripe;
import com.stripe.model.Charge;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.stripe.StripeModel;
import org.lpw.ranch.stripe.StripeService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(TransactionModel.NAME + ".service")
public class TransactionServiceImpl implements TransactionService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Numeric numeric;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private StripeService stripeService;
    @Inject
    private TransactionDao transactionDao;

    @Override
    public JSONObject query(String user, String[] creates) {
        return transactionDao.query(user, dateTime.toTimeRange(creates), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject charge(String key, int amount, String currency, String tradeNo) {
        StripeModel stripe = stripeService.findByKey(key);
        Stripe.apiKey = stripe.getSecret();
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("source", tradeNo);
        params.put("receipt_email", stripe.getEmail());
        try {
            Charge charge = Charge.create(params);
            if (!charge.getStatus().equals("succeeded")) {
                logger.warn(null, "Stripe充值[{}:{}]失败！", params, charge.toJson());

                return json.toObject(charge.toJson());
            }

            TransactionModel transaction = new TransactionModel();
            transaction.setKey(key);
            transaction.setUser(userHelper.id());
            transaction.setOrderNo(paymentHelper.create("stripe", stripe.getPublishable(), transaction.getUser(), amount,
                    "", null, null));
            transaction.setTradeNo(tradeNo);
            transaction.setAmount(amount);
            transaction.setCurrency(currency);
            transaction.setResponse(charge.toJson());
            transaction.setCreate(new Timestamp(charge.getCreated() * 1000));
            transaction.setFinish(dateTime.now());
            transactionDao.save(transaction);
            paymentHelper.complete(transaction.getOrderNo(), transaction.getAmount(), transaction.getTradeNo(), 1, null);

            return json.toObject(charge.toJson());
        } catch (Throwable throwable) {
            logger.warn(throwable, "创建Stripe充值时发生异常！");

            return null;
        }
    }
}
