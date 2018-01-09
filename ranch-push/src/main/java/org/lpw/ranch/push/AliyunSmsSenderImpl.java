package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.sms.aliyun")
public class AliyunSmsSenderImpl implements PushSender, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Value("${" + PushModel.NAME + ".sender.sms.aliyun.key:}")
    private String key;
    @Value("${" + PushModel.NAME + ".sender.sms.aliyun.secret:}")
    private String secret;
    private TaobaoClient taobaoClient;

    @Override
    public String getName() {
        return "sms.aliyun";
    }

    @Override
    public boolean send(PushModel push, String receiver, JSONObject args) {
        if (taobaoClient == null)
            return false;

        try {
            AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
            request.setSmsType("normal");
            request.setSmsFreeSignName(push.getName());
            if (!validator.isEmpty(args))
                request.setSmsParamString(args.toJSONString());
            request.setRecNum(receiver);
            request.setSmsTemplateCode(push.getTemplate());

            return taobaoClient.execute(request).isSuccess();
        } catch (Exception e) {
            logger.warn(e, "通过阿里云发送短信[{}:{}:{}]时发生异常！", receiver, modelHelper.toJson(push), args);

            return false;
        }
    }

    @Override
    public int getContextRefreshedSort() {
        return 31;
    }

    @Override
    public void onContextRefreshed() {
        if (validator.isEmpty(key) || validator.isEmpty(secret))
            return;

        taobaoClient = new DefaultTaobaoClient("", key, secret);
    }
}
