package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.push.model.v20160801.PushRequest;
import org.lpw.ranch.push.aliyun.AliyunModel;
import org.lpw.ranch.push.aliyun.AliyunService;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.app.aliyun")
public class AliyunAppSenderImpl implements PushSender {
    @Inject
    private Numeric numeric;
    @Inject
    private Json json;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private PushService pushService;
    @Inject
    private AliyunService aliyunService;

    @Override
    public String getName() {
        return "app.aliyun";
    }

    @Override
    public boolean send(PushModel push, String receiver, JSONObject args) {
        if (args == null || !args.containsKey("appCode"))
            return false;

        AliyunModel aliyun = aliyunService.find(args.getString("appCode"));
        PushRequest pushRequest = new PushRequest();
        pushRequest.setAppKey(numeric.toLong(aliyun.getAppKey()));
        pushRequest.setTarget("DEVICE");
        pushRequest.setTargetValue(receiver);
        pushRequest.setPushType("NOTICE");
        pushRequest.setDeviceType("ALL");
        pushRequest.setIOSApnsEnv(json.hasTrue(args, "product") ? "PRODUCT" : "DEV");
        pushRequest.setTitle(pushService.parse(PushService.Type.Subject, push.getKey(), push.getSubject(), args));
        pushRequest.setBody(pushService.parse(PushService.Type.Content, push.getKey(), push.getContent(), args));
        if (args.containsKey("openUrl")) {
            pushRequest.setAndroidOpenType("URL");
            pushRequest.setAndroidOpenUrl(args.getString("openUrl"));
        } else if (args.containsKey("activity")) {
            pushRequest.setAndroidOpenType("ACTIVITY");
            pushRequest.setAndroidOpenUrl(args.getString("activity"));
        } else
            pushRequest.setAndroidOpenType("APPLICATION");
        try {
            aliyunService.getIAcsClient(aliyun.getAppCode()).getAcsResponse(pushRequest);

            return true;
        } catch (ClientException e) {
            logger.warn(e, "使用阿里云APP推送[{}:{}:{}]时发生异常！", modelHelper.toJson(push), receiver, args);
        }

        return false;
    }
}
