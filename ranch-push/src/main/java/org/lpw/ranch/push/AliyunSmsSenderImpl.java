package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
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
    private IAcsClient acsClient;

    @Override
    public String getName() {
        return "sms.aliyun";
    }

    @Override
    public boolean send(PushModel push, String receiver, JSONObject args) {
        if (acsClient == null)
            return false;

        try {
            SendSmsRequest request = new SendSmsRequest();
            request.setMethod(MethodType.POST);
            request.setPhoneNumbers(receiver);
            request.setSignName(push.getName());
            request.setTemplateCode(push.getTemplate());
            if (!validator.isEmpty(args))
                request.setTemplateParam(args.toJSONString());
            boolean ok = "OK".equals(acsClient.getAcsResponse(request).getCode());
            if (ok) {
                if (logger.isDebugEnable())
                    logger.debug("通过阿里云成功发送短信[{}:{}:{}]。", modelHelper.toJson(push), receiver, args);
            } else
                logger.warn(null, "通过阿里云发送短信[{}:{}:{}]失败！", modelHelper.toJson(push), receiver, args);

            return ok;
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

        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", key, secret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
            acsClient = new DefaultAcsClient(profile);
            if (logger.isInfoEnable())
                logger.info("初始化阿里云短信接口[{}]完成。", key);
        } catch (Exception e) {
            logger.warn(e, "初始化阿里云短信接口时发生异常！");
        }
    }
}
