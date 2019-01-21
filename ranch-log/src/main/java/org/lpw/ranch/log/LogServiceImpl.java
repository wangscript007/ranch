package org.lpw.ranch.log;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService, DateJob {
    @Inject
    private Json json;
    @Inject
    private DateTime dateTime;
    @Inject
    private Header header;
    @Inject
    private Request request;
    @Inject
    private UserHelper userHelper;
    @Inject
    private LogDao logDao;

    @Override
    public void save(String type) {
        LogModel log = new LogModel();
        log.setType(type);
        log.setUser(userHelper.id());
        log.setIp(header.getIp());
        log.setHeader(json.toObject(header.getMap(), false).toJSONString());
        log.setParameter(json.toObject(request.getMap(), false).toJSONString());
        log.setTime(dateTime.now());
        logDao.save(log);
    }

    @Override
    public void executeDateJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        logDao.delete(dateTime.getStart(calendar.getTime()));
    }
}
