package org.lpw.ranch.payment;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.helper.AccountHelper;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Json;
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
@Service(PaymentModel.NAME + ".service")
public class PaymentServiceImpl implements PaymentService {
    private static final String LOCK_ORDER_NO = PaymentModel.NAME + ".service.order-no:";

    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
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
    private AccountHelper accountHelper;
    @Inject
    private PaymentDao paymentDao;
    private Set<String> ignores;

    public PaymentServiceImpl() {
        ignores = new HashSet<>();
        ignores.add("id");
        ignores.add("type");
        ignores.add("user");
        ignores.add("appId");
        ignores.add("amount");
        ignores.add("orderNo");
        ignores.add("tradeNo");
        ignores.add("notice");
        ignores.add("state");
        ignores.add("sign");
        ignores.add("sign-time");
    }

    @Override
    public JSONObject query(String type, String appId, String user, String orderNo, String tradeNo, int state, String start, String end) {
        JSONObject object = paymentDao.query(type, appId, userHelper.findIdByUid(user, user), orderNo, tradeNo, state, dateTime.getStart(start),
                dateTime.getEnd(end), pagination.getPageSize(20), pagination.getPageNum()).toJson();
        userHelper.fill(object.getJSONArray("list"), new String[]{"user"});

        return object;
    }

    @Override
    public JSONObject success(String id, Map<String, String> map) {
        PaymentModel payment = find(id);
        complete(payment, 1, "success", map);

        return modelHelper.toJson(payment);
    }

    @Override
    public JSONObject notice(String id) {
        PaymentModel payment = find(id);
        notice(payment);

        return modelHelper.toJson(payment);
    }

    @Override
    public PaymentModel find(String uk) {
        PaymentModel payment = paymentDao.findById(uk);

        return payment == null ? paymentDao.findByOrderNo(uk) : payment;
    }

    @Override
    public JSONObject create(String type, String appId, String user, int amount, String notice, Map<String, String> map) {
        PaymentModel payment = new PaymentModel();
        payment.setType(type);
        payment.setAppId(appId);
        payment.setUser(validator.isEmpty(user) ? userHelper.id() : user);
        payment.setAmount(amount);
        payment.setStart(dateTime.now());
        payment.setOrderNo(newOrderNo(payment.getStart()));
        payment.setTradeNo("");
        payment.setNotice(notice);
        JSONObject json = new JSONObject();
        json.put("create", putToJson(new JSONObject(), map));
        payment.setJson(json.toJSONString());
        paymentDao.save(payment);

        return modelHelper.toJson(payment);
    }

    private String newOrderNo(Timestamp now) {
        String time = dateTime.toString(now, "yyyyMMddHHmmssSSS");
        while (true) {
            String orderNo = time + generator.random(4);
            if (paymentDao.findByOrderNo(orderNo) == null)
                return orderNo;
        }
    }

    @Override
    public JSONObject complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map) {
        String lock = lockHelper.lock(LOCK_ORDER_NO + orderNo, 1000L);
        PaymentModel payment = find(orderNo);
        if (payment.getState() == 0) {
            payment.setAmount(amount);
            payment.setTradeNo(tradeNo);
            complete(payment, state, "complete", map);
        }
        lockHelper.unlock(lock);

        return modelHelper.toJson(payment);
    }

    private void complete(PaymentModel payment, int state, String name, Map<String, String> map) {
        payment.setState(state);
        payment.setEnd(dateTime.now());
        JSONObject json = this.json.toObject(payment.getJson());
        json.put(name, putToJson(new JSONObject(), map));
        payment.setJson(json.toJSONString());
        paymentDao.save(payment);
        if (state == 1)
            accountHelper.deposit(payment.getUser(), "", 0, payment.getType(), payment.getAmount(), true, merge(json.getJSONObject("create"), map));
        notice(payment);
    }

    private JSONObject putToJson(JSONObject object, Map<String, String> map) {
        map.forEach((key, value) -> {
            if (ignores.contains(key))
                return;

            object.put(key, value);
        });

        return object;
    }

    private Map<String, String> merge(JSONObject object, Map<String, String> map) {
        Map<String, String> m = new HashMap<>();
        if (!validator.isEmpty(object))
            for (String key : object.keySet())
                m.put(key, object.getString(key));
        if (!validator.isEmpty(map))
            m.putAll(map);

        return m;
    }

    private void notice(PaymentModel payment) {
        if (validator.isEmpty(payment.getNotice()))
            return;

        JSONObject notice = json.toObject(payment.getNotice());
        if (notice == null)
            return;

        Map<String, String> parameters = new HashMap<>();
        parameters.put("type", payment.getType());
        parameters.put("user", payment.getUser());
        parameters.put("amount", converter.toString(payment.getAmount(), "0"));
        parameters.put("orderNo", payment.getOrderNo());
        parameters.put("state", converter.toString(payment.getState(), "0"));
        parameters.put("start", dateTime.toString(payment.getStart()));
        parameters.put("end", dateTime.toString(payment.getEnd()));

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
