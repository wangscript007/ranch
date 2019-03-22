package org.lpw.ranch.access;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lpw
 */
@Service(AccessModel.NAME + ".service")
public class AccessServiceImpl implements AccessService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccessDao accessDao;
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Override
    public JSONObject query(String host, String uri, String user, String userAgent, String start, String end) {
        return accessDao.query(host, uri, userHelper.findIdByUid(user, user), userAgent, dateTime.getStart(start),
                dateTime.getEnd(end), pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void save(String host, String uri, String ip, String userAgent, String referer, Map<String, String> header) {
        AccessModel access = new AccessModel();
        access.setHost(validator.isEmpty(host) ? "" : host);
        access.setUri(uri);
        String user = userHelper.id();
        access.setUser(validator.isEmpty(user) ? "" : user);
        access.setIp(ip);
        access.setUserAgent(userAgent);
        access.setReferer(referer);
        access.setHeader(header.toString());
        access.setTime(dateTime.now());
        executorService.submit(() -> accessDao.save(access));
    }
}
