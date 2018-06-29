package org.lpw.ranch.access;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(AccessModel.NAME + ".service")
public class AccessServiceImpl implements AccessService, MinuteJob, DateJob {
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
    private List<AccessModel> list = Collections.synchronizedList(new ArrayList<>());

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
        list.add(access);
    }

    @Override
    public void executeMinuteJob() {
        List<AccessModel> list = new ArrayList<>(this.list);
        this.list.clear();
        list.forEach(accessDao::save);
    }

    @Override
    public void executeDateJob() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -3);
        accessDao.delete(dateTime.getStart(calendar.getTime()));
    }
}
