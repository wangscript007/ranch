package org.lpw.ranch.logger;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LoggerModel.NAME + ".service")
public class LoggerServiceImpl implements LoggerService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Pagination pagination;
    @Inject
    private LoggerDao loggerDao;

    @Override
    public JSONObject query(String key, int state, String start, String end) {
        return loggerDao.query(key, state, dateTime.toTime(start, "yyyy-MM-dd HH:mm:ss"), dateTime.toTime(end,
                "yyyy-MM-dd HH:mm:ss"), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void create(String key, String... ps) {
        LoggerModel logger = new LoggerModel();
        logger.setKey(key);
        logger.setP0(getParameter(ps, 0));
        logger.setP1(getParameter(ps, 1));
        logger.setP2(getParameter(ps, 2));
        logger.setP3(getParameter(ps, 3));
        logger.setP4(getParameter(ps, 4));
        logger.setP5(getParameter(ps, 5));
        logger.setP6(getParameter(ps, 6));
        logger.setP7(getParameter(ps, 7));
        logger.setP8(getParameter(ps, 8));
        logger.setP9(getParameter(ps, 9));
        logger.setTime(dateTime.now());
        loggerDao.save(logger);
    }

    private String getParameter(String[] ps, int index) {
        if (ps.length <= index)
            return null;

        if (ps[index].length() <= 100)
            return ps[index];

        return ps[index].substring(0, 100);
    }

    @Override
    public void state(String id, int state) {
        LoggerModel logger = loggerDao.findById(id);
        if (logger == null)
            return;

        logger.setState(state);
        loggerDao.save(logger);
    }
}
