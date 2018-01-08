package org.lpw.ranch.push;

import org.springframework.stereotype.Service;
import com.taobao.api.TaobaoClient;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".sender.sms.aliyun")
public class AliyunSmsSenderImpl implements PushSender {
    @Override
    public String getName() {
        return "sms.aliyun";
    }

    @Override
    public boolean send(String receiver, String subject, String content) {
        TaobaoClient client;

        return false;
    }
}
