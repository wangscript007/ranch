package org.lpw.ranch.push.log;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.device.helper.DeviceHelper;
import org.lpw.ranch.push.PushModel;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService {
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private DeviceHelper deviceHelper;
    @Inject
    private LogDao logDao;

    @Override
    public LogModel create(String receiver, PushModel push, JSONObject args) {
        LogModel log = new LogModel();
        log.setUser(deviceHelper.find(push.getAppCode(), receiver).getString("user"));
        log.setReceiver(receiver);
        log.setAppCode(push.getAppCode());
        log.setSender(push.getSender());
        log.setPush(modelHelper.toJson(push).toJSONString());
        if (args != null)
            log.setArgs(args.toJSONString());
        logDao.save(log);

        return log;
    }

    @Override
    public void send(LogModel log, boolean success) {
        logDao.setState(log.getId(), success ? 1 : 3);
    }

    @Override
    public int unread(String receiver) {
        return logDao.count(receiver, 1);
    }
}
