package org.lpw.ranch.alipay;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.AlipayRequest;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayFundTransOrderQueryRequest;
import com.alipay.api.request.AlipayFundTransToaccountTransferRequest;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayFundTransOrderQueryResponse;
import org.lpw.ranch.payment.helper.PaymentHelper;
import org.lpw.ranch.transfer.helper.TransferHelper;
import org.lpw.ranch.transfer.helper.TransferListener;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Coder;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(AlipayModel.NAME + ".service")
public class AlipayServiceImpl implements AlipayService, TransferListener {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private Numeric numeric;
    @Inject
    private Coder coder;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PaymentHelper paymentHelper;
    @Inject
    private TransferHelper transferHelper;
    @Inject
    private AlipayDao alipayDao;
    @Value("${tephra.ctrl.service-root:}")
    private String root;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(alipayDao.query().getList());
    }

    @Override
    public AlipayModel findByKey(String key) {
        return alipayDao.findByKey(key);
    }

    @Override
    public AlipayModel findByAppId(String appId) {
        return alipayDao.findByAppId(appId);
    }

    @Override
    public JSONObject save(AlipayModel alipay) {
        AlipayModel model = alipayDao.findByKey(alipay.getKey());
        if (model == null) {
            model = new AlipayModel();
            model.setKey(alipay.getKey());
        }
        model.setName(alipay.getName());
        model.setAppId(alipay.getAppId());
        model.setPrivateKey(alipay.getPrivateKey());
        model.setPublicKey(alipay.getPublicKey());
        alipayDao.save(model);

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String id) {
        alipayDao.delete(id);
    }

    @Override
    public String quickWapPay(String key, String user, String subject, int amount, String billNo, String notice, String returnUrl) {
        AlipayModel alipay = alipayDao.findByKey(key);
        String content = getBizContent(alipay.getAppId(), user, subject, amount, billNo, notice, "QUICK_WAP_PAY");
        if (content == null)
            return null;

        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setBizContent(content);

        return prepay(alipay, returnUrl, request);
    }

    @Override
    public String fastInstantTradePay(String key, String user, String subject, int amount, String billNo, String notice, String returnUrl) {
        AlipayModel alipay = alipayDao.findByKey(key);
        String content = getBizContent(alipay.getAppId(), user, subject, amount, billNo, notice, "FAST_INSTANT_TRADE_PAY");
        if (content == null)
            return null;

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizContent(content);

        return prepay(alipay, returnUrl, request);
    }

    @Override
    public String quickMsecurityPay(String key, String user, String subject, int amount, String billNo, String notice) {
        AlipayModel alipay = alipayDao.findByKey(key);
        String content = getBizContent(alipay.getAppId(), user, subject, amount, billNo, notice, "QUICK_MSECURITY_PAY");
        if (content == null)
            return null;

        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        request.setBizContent(content);
        String string = prepay(alipay, null, request);

        return string == null ? null : string.substring(string.indexOf('?') + 1, string.indexOf('>') - 1) + "&biz_content=" +
                coder.encodeUrl(string.substring(string.indexOf('{'), string.indexOf('}') + 1)
                        .replaceAll("&quot;", "\""), null);
    }

    private String getBizContent(String appId, String user, String subject, int amount, String billNo, String notice, String code) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        String orderNo = paymentHelper.create(getType(), appId, user, amount, billNo, notice, null);
        if (validator.isEmpty(orderNo))
            return null;

        JSONObject object = new JSONObject();
        object.put("out_trade_no", orderNo);
        object.put("subject", subject);
        object.put("total_amount", numeric.toString(amount * 0.01D, "0.00"));
        object.put("product_code", code);

        return object.toJSONString();
    }

    private String prepay(AlipayModel alipay, String returnUrl, AlipayRequest<? extends AlipayResponse> request) {
        request.setNotifyUrl(root + "/alipay/notice");
        if (!validator.isEmpty(returnUrl))
            request.setReturnUrl(returnUrl);

        try {
            return newAlipayClient(alipay).pageExecute(request).getBody();
        } catch (Throwable e) {
            logger.warn(e, "执行支付宝付款时发生异常！");

            return null;
        }
    }

    @Override
    public boolean notice(String appId, String orderNo, String tradeNo, String amount, String status, Map<String, String> map) {
        if (status.equals("WAIT_BUYER_PAY"))
            return false;

        AlipayModel alipay = alipayDao.findByAppId(appId);
        if (alipay == null)
            return false;

        try {
            if (!AlipaySignature.rsaCheckV1(map, alipay.getPublicKey(), "UTF-8", map.get(AlipayConstants.SIGN_TYPE)))
                return failure(null, alipay, map);
        } catch (AlipayApiException e) {
            return failure(e, alipay, map);
        }

        int state = status.equals("TRADE_SUCCESS") || status.equals("TRADE_FINISHED") ? 1 : 0;

        return orderNo.equals(paymentHelper.complete(orderNo, getAmount(amount), tradeNo, state, map));
    }

    private boolean failure(Throwable e, AlipayModel alipay, Map<String, String> map) {
        logger.warn(e, "验证支付宝异步通知[{}:{}]签名失败！", modelHelper.toString(alipay), converter.toString(map));

        return false;

    }

    private int getAmount(String amount) {
        int indexOf = amount.indexOf('.');
        if (indexOf == -1)
            return numeric.toInt(amount) * 100;

        return numeric.toInt(amount.substring(0, indexOf)) * 100 + numeric.toInt(amount.substring(indexOf + 1)) % 100;
    }

    @Override
    public boolean transfer(String key, String user, String account, int amount, String billNo, String realName, String showName,
                            String remark, String notice, Map<String, String> map) {
        AlipayModel alipay = alipayDao.findByKey(key);
        String orderNo = transferHelper.create(getType(), alipay.getAppId(), validator.isEmpty(user) ? userHelper.id() : user,
                account, amount, billNo, notice, map);
        if (validator.isEmpty(orderNo))
            return false;

        JSONObject content = new JSONObject();
        content.put("out_biz_no", orderNo);
        content.put("payee_type", "ALIPAY_LOGONID");
        content.put("payee_account", account);
        content.put("amount", numeric.toString(amount * 0.01D, "0.00"));
        if (!validator.isEmpty(showName))
            content.put("payer_show_name", showName);
        if (!validator.isEmpty(realName))
            content.put("payee_real_name", realName);
        if (!validator.isEmpty(remark))
            content.put("remark", remark);
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
        request.setBizContent(content.toJSONString());
        try {
            return newAlipayClient(alipay).execute(request).isSuccess();
        } catch (AlipayApiException e) {
            logger.warn(e, "发送支付宝转账请求[{}]时发生异常！", content.toJSONString());

            return false;
        }
    }

    @Override
    public String getType() {
        return "alipay";
    }

    @Override
    public void resetState(JSONObject object) {
        AlipayModel alipay = findByAppId(object.getString("appId"));
        if (alipay == null)
            return;

        JSONObject content = new JSONObject();
        String orderNo = object.getString("orderNo");
        content.put("out_biz_no", orderNo);
        AlipayFundTransOrderQueryRequest request = new AlipayFundTransOrderQueryRequest();
        request.setBizContent(content.toJSONString());
        try {
            AlipayFundTransOrderQueryResponse response = newAlipayClient(alipay).execute(request);
            if (!response.isSuccess())
                return;

            String tradeNo = response.getOrderId();
            if (validator.isEmpty(tradeNo))
                return;

            int state = getState(response.getStatus());
            if (state == 0)
                return;

            transferHelper.complete(orderNo, object.getIntValue("amount"), tradeNo, state, null);
        } catch (AlipayApiException e) {
            logger.warn(e, "查询支付宝转账[{}]结果时发生异常！", object.toJSONString());
        }
    }

    private int getState(String status) {
        switch (status) {
            case "SUCCESS":
                return 1;
            case "FAIL":
                return 2;
            default:
                return 0;
        }
    }

    private AlipayClient newAlipayClient(AlipayModel alipay) {
        return new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", alipay.getAppId(), alipay.getPrivateKey(),
                "json", "UTF-8", alipay.getPublicKey(), AlipayConstants.SIGN_TYPE_RSA2);
    }
}
