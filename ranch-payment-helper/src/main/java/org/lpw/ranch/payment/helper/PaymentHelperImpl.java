package org.lpw.ranch.payment.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.util.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.payment.helper")
public class PaymentHelperImpl implements PaymentHelper {
    @Inject
    private Converter converter;
    @Inject
    private Request request;
    @Inject
    private Carousel carousel;
    @Value("${ranch.payment.key:ranch.payment}")
    private String paymentKey;

    @Override
    public String create(String type, String appId, String user, int amount, String notice) {
        Map<String, String> parameter = new HashMap<>(request.getMap());
        parameter.put("type", type);
        parameter.put("appId", appId);
        parameter.put("user", user);
        parameter.put("amount", converter.toString(amount, "0"));
        parameter.put("notice", notice);
        JSONObject object = carousel.service(paymentKey + ".create", null, parameter, false, JSONObject.class);

        return object.getString("orderNo");
    }

    @Override
    public String complete(String orderNo, int amount, String tradeNo, int state) {
        Map<String, String> parameter = new HashMap<>(request.getMap());
        parameter.put("orderNo", orderNo);
        parameter.put("amount", converter.toString(amount, "0"));
        parameter.put("tradeNo", tradeNo);
        parameter.put("state", converter.toString(state, "0"));
        JSONObject object = carousel.service(paymentKey + ".complete", null, parameter, false, JSONObject.class);

        return object.getString("orderNo");
    }
}
