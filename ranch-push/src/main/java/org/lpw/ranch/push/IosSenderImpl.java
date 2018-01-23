package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.lpw.ranch.push.ios.IosModel;
import org.lpw.ranch.push.ios.IosService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.app.ios")
public class IosSenderImpl implements PushSender {
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
        if (args == null || !args.containsKey("appCode"))
            return false;

        IosModel ios = iosService.find(args.getString("appCode"));
        if (ios == null)
            return false;

        ApnsService service = iosService.getApnsService(ios.getAppCode());
        if (service == null)
            return false;

        String message = pushService.parse(PushService.Type.Content, push.getKey(), push.getContent(), args);
        service.push(receiver, APNS.newPayload().alertBody(message).build());

        return true;
    }
}
