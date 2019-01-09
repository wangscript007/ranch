package org.lpw.ranch.appstore.transaction;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.appstore.AppstoreModel;
import org.lpw.ranch.appstore.AppstoreService;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(TransactionModel.NAME + ".service")
public class TransactionServiceImpl implements TransactionService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private AppstoreService appstoreService;
    @Inject
    private TransactionDao transactionDao;

    @Override
    public JSONObject query(String user, String transactionId, String productId, String[] times) {
        return transactionDao.query(user, transactionId, productId, dateTime.toTimeRange(times),
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void save(String receipt, JSONObject object) {
        String transactionId = object.getString("transaction_id");
        if (transactionDao.findByTransactionId(transactionId) != null)
            return;

        TransactionModel transaction = new TransactionModel();
        transaction.setProductId(object.getString("product_id"));
        AppstoreModel appstore = appstoreService.findByProductId(transaction.getProductId());
        if (appstore == null)
            return;

        transaction.setPrice(appstore.getAmount());
        transaction.setQuantity(object.getIntValue("quantity"));
        transaction.setAmount(transaction.getPrice() * transaction.getQuantity());
        if (transaction.getAmount() <= 0)
            return;

        transaction.setUser(userHelper.id());
        transaction.setReceipt(receipt);
        transaction.setTransactionId(transactionId);
        transaction.setTime(dateTime.now());
        transactionDao.save(transaction);
        String orderNo = paymentHelper.create("app-store", transaction.getProductId(), "", transaction.getAmount(),
                "", "", null);
        paymentHelper.complete(orderNo, transaction.getAmount(), transactionId, 1, null);
    }
}
