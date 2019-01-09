package org.lpw.ranch.appstore.receipt;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.appstore.transaction.TransactionService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

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
    private UserHelper userHelper;
    @Inject
    private TransactionService transactionService;
    @Inject
    private ReceiptDao receiptDao;

    @Override
    public JSONObject query(String user, String[] times) {
        return receiptDao.query(user, dateTime.toTimeRange(times), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject verify(String data) {
        JSONObject object = verifyReceipt("https://buy.itunes.apple.com/verifyReceipt", data);
        if (object == null || object.getIntValue("status") == 21007)
            object = verifyReceipt("https://sandbox.itunes.apple.com/verifyReceipt", data);
        if (object == null)
            return null;

        ReceiptModel receipt = new ReceiptModel();
        receipt.setUser(userHelper.id());
        receipt.setStatus(object.getIntValue("status"));
        receipt.setRequest(data);
        receipt.setResponse(object.toJSONString());
        receipt.setTime(dateTime.now());
        receiptDao.save(receipt);
        if (receipt.getStatus() == 0)
            success(receipt, object.getJSONObject("receipt"));

        return object;
    }

    private JSONObject verifyReceipt(String url, String receiptData) {
        JSONObject parameter = new JSONObject();
        parameter.put("receipt-data", receiptData);
        String string = http.post(url, null, parameter.toJSONString());
        JSONObject object = json.toObject(string);
        if (object == null)
            logger.warn(null, "校验AppStore[{}]收据[{}:{}]失败！", url, parameter, string);

        return object;
    }

    private void success(ReceiptModel receipt, JSONObject object) {
        if (!json.containsKey(object, "in_app"))
            return;

        JSONArray array = object.getJSONArray("in_app");
        for (int i = 0, size = array.size(); i < size; i++)
            transactionService.save(receipt.getId(), array.getJSONObject(i));
    }
}
