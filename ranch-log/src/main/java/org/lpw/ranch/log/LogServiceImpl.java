package org.lpw.ranch.log;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Map;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService, DateJob {
    @Inject
    private DateTime dateTime;
    @Inject
    private Header header;
    @Inject
    private Request request;
    @Inject
    private Session session;
    @Inject
    private UserHelper userHelper;
    @Inject
    private LogDao logDao;

    @Override
    public void save(String type) {
        LogModel log = new LogModel();
        log.setType(type);
        log.setSid(session.getId());
        log.setUser(userHelper.id());
        log.setIp(header.getIp());
        log.setReferer(header.get("referer"));
        log.setHeader(toJson(header.getMap()));
        log.setParameter(toJson(request.getMap()));
        log.setTime(dateTime.now());
        logDao.save(log);
    }

    private String toJson(Map<String, String> map) {
        JSONObject object = new JSONObject();
        map.forEach(object::put);

        return object.toJSONString();
    }

    @Override
    public void executeDateJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        logDao.delete(dateTime.getStart(calendar.getTime()));
    }
}
