package org.lpw.ranch.push.log;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.device.helper.DeviceHelper;
import org.lpw.ranch.push.PushModel;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService {
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private DeviceHelper deviceHelper;
    @Inject
    private LogDao logDao;

    @Override
    public JSONObject query(String user, String receiver, String appCode, String sender, int state, String start, String end) {
        JSONObject object = logDao.query(user, receiver, appCode, sender, state, dateTime.getStart(start), dateTime.getEnd(end),
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
        userHelper.fill(object.getJSONArray("list"), new String[]{"user"});

        return object;
    }

    @Override
    public JSONObject query(String receiver, String appCode) {
        return logDao.query(receiver, appCode, 1, 2, pagination.getPageSize(20),
                pagination.getPageNum()).toJson();
    }

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
        logDao.setState(log.getId(), 0, success ? 1 : 3);
    }

    @Override
    public int unread(String receiver, String appCode) {
        return logDao.count(receiver, appCode, 1);
    }

    @Override
    public void read(String id) {
        logDao.setState(id, 1, 2);
    }

    @Override
    public void reads(String receiver, String appCode) {
        logDao.setState(receiver, appCode, 1, 2);
    }
}
