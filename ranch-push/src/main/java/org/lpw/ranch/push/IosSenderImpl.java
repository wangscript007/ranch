package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.PayloadBuilder;
import org.lpw.ranch.push.ios.IosModel;
import org.lpw.ranch.push.ios.IosService;
import org.lpw.tephra.util.Json;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.app.ios")
public class IosSenderImpl implements PushSender {
    @Inject
    private Json json;
    @Inject
    private PushService pushService;
    @Inject
    private IosService iosService;

    @Override
    public String getName() {
        return "app.ios";
    }

    @Override
    public boolean send(PushModel push, String receiver, JSONObject args) {
        IosModel ios = iosService.find(push.getAppCode(), args.getIntValue("destination"));
        if (ios == null)
            return false;

        ApnsService service = iosService.getApnsService(ios.getAppCode(), ios.getDestination());
        if (service == null)
            return false;

        PayloadBuilder payloadBuilder = APNS.newPayload();
        payloadBuilder.alertTitle(pushService.parse(PushService.Type.Subject, push.getKey(), push.getSubject(), args));
        payloadBuilder.alertBody(pushService.parse(PushService.Type.Content, push.getKey(), push.getContent(), args));
        payloadBuilder.badge((json.containsKey(args, "badge") ? args.getIntValue("badge") : 1) + 1);
        payloadBuilder.sound(json.containsKey(args, "sound") ? args.getString("sound") : "default");
        service.push(receiver, payloadBuilder.build());

        return true;
    }
}
