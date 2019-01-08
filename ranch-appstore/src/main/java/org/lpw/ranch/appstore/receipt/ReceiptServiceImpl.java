package org.lpw.ranch.appstore.receipt;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.appstore.AppstoreModel;
import org.lpw.ranch.appstore.AppstoreService;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ReceiptModel.NAME + ".service")
public class ReceiptServiceImpl implements ReceiptService {
    @Inject
    private Http http;
    @Inject
    private Json json;
    @Inject
    private Digest digest;
    @Inject
    private DateTime dateTime;
    @Inject
    private Logger logger;
    @Inject
    private Pagination pagination;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private AppstoreService appstoreService;
    @Inject
    private ReceiptDao receiptDao;

    @Override
    public JSONObject query(String productId, String[] times) {
        return receiptDao.query(productId, dateTime.toTimeRange(times), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject verify(String data) {
        JSONObject object = verifyReceipt("https://buy.itunes.apple.com/verifyReceipt", data);
        if (object == null)
            object = verifyReceipt("https://sandbox.itunes.apple.com/verifyReceipt", data);
        if (object == null)
            return null;

        ReceiptModel receipt = new ReceiptModel();
        receipt.setStatus(object.getIntValue("status"));
        receipt.setMd5(digest.md5(data));
        receipt.setRequest(data);
        receipt.setResponse(object.toJSONString());
        receipt.setTime(dateTime.now());
        if (receipt.getStatus() == 0)
            success(receipt, object.getJSONObject("receipt"));
        receiptDao.save(receipt);

        return object;
    }

    private JSONObject verifyReceipt(String url, String receiptData) {
        Map<String, String> map = new HashMap<>();
        map.put("receipt-data", receiptData);
        String string = http.post(url, null, map);
        JSONObject object = json.toObject(string);
        if (object == null)
            logger.warn(null, "校验AppStore[{}]收据[{}:{}]失败！", url, map, string);

        return object;
    }

    private void success(ReceiptModel receipt, JSONObject object) {
        receipt.setProductId(object.getString("product_id"));
        AppstoreModel appstore = appstoreService.findByProductId(receipt.getProductId());
        if (appstore != null)
            receipt.setPrice(appstore.getAmount());
        receipt.setQuantity(object.getIntValue("quantity"));
        receipt.setAmount(receipt.getPrice() * receipt.getQuantity());
        if (receipt.getAmount() <= 0 || receiptDao.find(receipt.getMd5(), 0) != null)
            return;

        String orderNo = paymentHelper.create("app-store", receipt.getProductId(), "", receipt.getAmount(),
                receipt.getMd5(), "", null);
        paymentHelper.complete(orderNo, receipt.getAmount(), object.getString("transaction_id"), 1, null);
    }
}
