package org.lpw.ranch.transfer;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Service(TransferModel.NAME + ".service")
public class TransferServiceImpl implements TransferService {
    private static final String LOCK_ORDER_NO = TransferModel.NAME + ".service.order-no:";

    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private Json json;
    @Inject
    private Http http;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private Carousel carousel;
    @Inject
    private UserHelper userHelper;
    @Inject
    private TransferDao transferDao;
    private Set<String> ignores;

    public TransferServiceImpl() {
        ignores = new HashSet<>();
        ignores.add("id");
        ignores.add("type");
        ignores.add("appId");
        ignores.add("user");
        ignores.add("account");
        ignores.add("amount");
        ignores.add("orderNo");
        ignores.add("billNo");
        ignores.add("tradeNo");
        ignores.add("notice");
        ignores.add("state");
        ignores.add("sign");
        ignores.add("sign-time");
    }

    @Override
    public JSONObject query(String type, String appId, String user, String orderNo, String billNo, String tradeNo,
                            int state, String start, String end) {
        JSONObject object = transferDao.query(type, appId, userHelper.findIdByUid(user, user), orderNo, billNo, tradeNo,
                state, dateTime.getStart(start), dateTime.getEnd(end), pagination.getPageSize(20), pagination.getPageNum()).toJson();
        userHelper.fill(object.getJSONArray("list"), new String[]{"user"});

        return object;
    }

    @Override
    public JSONObject success(String id, Map<String, String> map) {
        TransferModel transfer = find(id);
        complete(transfer, 1, "success", map);

        return modelHelper.toJson(transfer);
    }

    @Override
    public JSONObject notice(String id) {
        TransferModel transfer = find(id);
        notice(transfer);

        return modelHelper.toJson(transfer);
    }

    @Override
    public TransferModel find(String uk) {
        TransferModel transfer = transferDao.findById(uk);

        return transfer == null ? transferDao.findByOrderNo(uk) : transfer;
    }

    @Override
    public JSONObject create(String type, String appId, String user, String account, int amount, String billNo,
                             String notice, Map<String, String> map) {
        TransferModel transfer = new TransferModel();
        transfer.setType(type);
        transfer.setAppId(appId);
        transfer.setUser(validator.isEmpty(user) ? userHelper.id() : user);
        transfer.setAccount(account);
        transfer.setAmount(amount);
        transfer.setStart(dateTime.now());
        transfer.setOrderNo(newOrderNo(transfer.getStart()));
        transfer.setBillNo(billNo == null ? "" : billNo);
        transfer.setTradeNo("");
        transfer.setNotice(notice);
        JSONObject json = new JSONObject();
        json.put("create", putToJson(new JSONObject(), map));
        transfer.setJson(json.toJSONString());
        transferDao.save(transfer);

        return modelHelper.toJson(transfer);
    }

    private String newOrderNo(Timestamp now) {
        String time = dateTime.toString(now, "yyyyMMddHHmmssSSS");
        while (true) {
            String orderNo = time + generator.random(4);
            if (transferDao.findByOrderNo(orderNo) == null)
                return orderNo;
        }
    }

    @Override
    public JSONObject complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map) {
        String lockId = lockHelper.lock(LOCK_ORDER_NO + orderNo, 1L, 0);
        if (lockId == null)
            return new JSONObject();

        TransferModel transfer = find(orderNo);
        if (transfer.getState() == 0) {
            transfer.setAmount(amount);
            transfer.setTradeNo(tradeNo);
            complete(transfer, state, "complete", map);
        }
        lockHelper.unlock(lockId);

        return modelHelper.toJson(transfer);
    }

    private void complete(TransferModel transfer, int state, String name, Map<String, String> map) {
        transfer.setState(state);
        transfer.setEnd(dateTime.now());
        JSONObject json = this.json.toObject(transfer.getJson());
        json.put(name, putToJson(new JSONObject(), map));
        transfer.setJson(json.toJSONString());
        transferDao.save(transfer);
        notice(transfer);
    }

    private JSONObject putToJson(JSONObject object, Map<String, String> map) {
        map.forEach((key, value) -> {
            if (ignores.contains(key))
                return;

            object.put(key, value);
        });

        return object;
    }

    private void notice(TransferModel transfer) {
        if (validator.isEmpty(transfer.getNotice()))
            return;

        JSONObject notice = json.toObject(transfer.getNotice());
        if (notice == null)
            return;

        Map<String, String> parameters = new HashMap<>();
        parameters.put("type", transfer.getType());
        parameters.put("user", transfer.getUser());
        parameters.put("amount", numeric.toString(transfer.getAmount(), "0"));
        parameters.put("orderNo", transfer.getOrderNo());
        parameters.put("state", numeric.toString(transfer.getState(), "0"));
        parameters.put("start", dateTime.toString(transfer.getStart()));
        parameters.put("end", dateTime.toString(transfer.getEnd()));

        JSONObject params = notice.getJSONObject("params");
        if (!validator.isEmpty(params))
            for (String key : params.keySet())
                parameters.put(key, params.getString(key));

        if (!validator.isEmpty(notice.getString("service")))
            carousel.service(notice.getString("service"), null, parameters, false);

        if (!validator.isEmpty(notice.getString("http")))
            http.post(notice.getString("http"), null, parameters);
    }
}
