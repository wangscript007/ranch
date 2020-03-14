package org.lpw.ranch.transfer.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.scheduler.SecondsJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Numeric;
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
@Service("ranch.transfer.helper")
public class TransferHelperImpl implements TransferHelper, SecondsJob, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private Numeric numeric;
    @Inject
    private DateTime dateTime;
    @Inject
    private Sign sign;
    @Inject
    private Request request;
    @Inject
    private Carousel carousel;
    @Inject
    private LockHelper lockHelper;
    @Value("${ranch.transfer.key:ranch.transfer}")
    private String key;
    private Map<String, TransferListener> listeners;

    @Override
    public String create(String type, String appId, String user, String account, int amount, String billNo, String notice, Map<String, String> map) {
        Map<String, String> parameter = getParameter(map);
        parameter.put("type", type);
        parameter.put("appId", appId);
        parameter.put("user", user);
        parameter.put("account", account);
        parameter.put("amount", numeric.toString(amount, "0"));
        parameter.put("billNo", billNo);
        parameter.put("notice", notice);
        JSONObject object = carousel.service(key + ".create", null, parameter, false, JSONObject.class);

        return object.getString("orderNo");
    }

    @Override
    public String complete(String orderNo, int amount, String tradeNo, int state, Map<String, String> map) {
        Map<String, String> parameter = getParameter(map);
        parameter.put("orderNo", orderNo);
        parameter.put("amount", numeric.toString(amount, "0"));
        parameter.put("tradeNo", tradeNo);
        parameter.put("state", numeric.toString(state, "0"));
        sign.put(parameter, null);
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

        String lockId = lockHelper.lock("ranch.transfer.helper.seconds", 1L, 10);
        if (lockId == null)
            return;

        Map<String, String> parameter = new HashMap<>();
        parameter.put("start", dateTime.toString(new Date(System.currentTimeMillis() - TimeUnit.Day.getTime())));
        parameter.put("state", "0");
        parameter.put("pageSize", "1024");
        parameter.put("pageNum", "1");
        sign.put(parameter, null);
        JSONArray array = carousel.service(key + ".query", null, parameter, false, JSONObject.class).getJSONArray("list");
        if (validator.isEmpty(array)) {
            lockHelper.unlock(lockId);

            return;
        }

        for (int i = 0, size = array.size(); i < size; i++) {
            JSONObject object = array.getJSONObject(i);
            String type = object.getString("type");
            if (listeners.containsKey(type))
                listeners.get(type).resetState(object);
        }
        lockHelper.unlock(lockId);
    }

    @Override
    public int getContextRefreshedSort() {
        return 30;
    }

    @Override
    public void onContextRefreshed() {
        listeners = new HashMap<>();
        BeanFactory.getBeans(TransferListener.class).forEach(listener -> listeners.put(listener.getType(), listener));
    }
}
