package org.lpw.ranch.payment.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.scheduler.SecondsJob;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.payment.helper")
public class PaymentHelperImpl implements PaymentHelper, SecondsJob, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private DateTime dateTime;
    @Inject
    private Request request;
    @Inject
    private Carousel carousel;
    @Inject
    private LockHelper lockHelper;
    @Value("${ranch.payment.key:ranch.payment}")
    private String key;
    private Map<String, PaymentListener> listeners;

    @Override
    public String create(String type, String appId, String user, int amount, String notice, Map<String, String> map) {
        Map<String, String> parameter = getParameter(map);
        parameter.put("type", type);
        parameter.put("appId", appId);
        parameter.put("user", user);
        parameter.put("amount", converter.toString(amount, "0"));
        parameter.put("notice", notice);
        JSONObject object = carousel.service(key + ".create", null, parameter, false, JSONObject.class);

        return object.getString("orderNo");
    }

    @Override
    public String complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map) {
        Map<String, String> parameter = getParameter(map);
        parameter.put("orderNo", orderNo);
        parameter.put("amount", converter.toString(amount, "0"));
        parameter.put("tradeNo", tradeNo);
        parameter.put("state", converter.toString(state, "0"));
        JSONObject object = carousel.service(key + ".complete", null, parameter, false, JSONObject.class);

        return object.getString("orderNo");
    }

    private Map<String, String> getParameter(Map<String, String> map) {
        Map<String, String> parameter = new HashMap<>();
        if (!validator.isEmpty(request.getMap()))
            parameter.putAll(request.getMap());
        if (!validator.isEmpty(map))
            parameter.putAll(map);

        return parameter;
    }

    @Override
    public void executeSecondsJob() {
        if (validator.isEmpty(listeners) || Calendar.getInstance().get(Calendar.SECOND) % 30 > 0)
            return;

        String lockId = lockHelper.lock("ranch.payment.helper.seconds", 1L);
        Map<String, String> parameter = new HashMap<>();
        parameter.put("start", dateTime.toString(new Date(System.currentTimeMillis() - TimeUnit.Day.getTime())));
        parameter.put("state", "0");
        parameter.put("pageSize", "1024");
        parameter.put("pageNum", "1");
        JSONArray array = carousel.service(key + ".query", null, parameter, false, JSONObject.class).getJSONArray("list");
        if (array.isEmpty()) {
            lockHelper.unlock(lockId);

            return;
        }

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String type = object.getString("type");
            if (listeners.containsKey(type))
                listeners.get(type).resetState(object, System.currentTimeMillis() - dateTime.toTime(object.getString("start")).getTime() > 5 * TimeUnit.Minute.getTime());
        }
        lockHelper.unlock(lockId);
    }

    @Override
    public int getContextRefreshedSort() {
        return 25;
    }

    @Override
    public void onContextRefreshed() {
        listeners = new HashMap<>();
        BeanFactory.getBeans(PaymentListener.class).forEach(listener -> listeners.put(listener.getType(), listener));
    }
}
