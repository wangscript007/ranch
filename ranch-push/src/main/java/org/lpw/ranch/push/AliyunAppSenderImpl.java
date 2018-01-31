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
        AliyunModel aliyun = aliyunService.find(push.getAppCode());
        if (aliyun == null)
            return false;

        PushRequest pushRequest = new PushRequest();
        pushRequest.setAppKey(numeric.toLong(aliyun.getAppKey()));
        pushRequest.setTarget("DEVICE");
        pushRequest.setTargetValue(receiver);
        pushRequest.setPushType("NOTICE");
        pushRequest.setDeviceType("ALL");
        pushRequest.setTitle(pushService.parse(PushService.Type.Subject, push.getKey(), push.getSubject(), args));
        pushRequest.setBody(pushService.parse(PushService.Type.Content, push.getKey(), push.getContent(), args));
        pushRequest.setIOSApnsEnv(json.hasTrue(args, "product") ? "PRODUCT" : "DEV");
        pushRequest.setIOSBadge((json.containsKey(args, "badge") ? args.getIntValue("badge") : 1) + 1);
        pushRequest.setIOSMusic(json.containsKey(args, "ios-music") ? args.getString("ios-music") : "default");
        pushRequest.setAndroidMusic(json.containsKey(args, "android-music") ? args.getString("android-music") : "default");
        if (json.containsKey(args, "url")) {
            pushRequest.setAndroidOpenType("URL");
            pushRequest.setAndroidOpenUrl(args.getString("url"));
        } else if (json.containsKey(args, "activity")) {
            pushRequest.setAndroidOpenType("ACTIVITY");
            pushRequest.setAndroidActivity(args.getString("activity"));
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
