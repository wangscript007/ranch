package org.lpw.ranch.notice;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(NoticeModel.NAME + ".service")
public class NoticeServiceImpl implements NoticeService {
    @Inject
    private Cache cache;
    @Inject
    private Generator generator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private NoticeDao noticeDao;
    private Map<String, String> random = new ConcurrentHashMap<>();

    @Override
    public JSONObject global(String type, int read) {
        int pageSize = pagination.getPageSize(20);

        return cache.computeIfAbsent(getCacheKey("", type, read, pageSize, pagination.getPageNum()),
                key -> noticeDao.query("", type, read, pageSize, pagination.getPageNum()).toJson(), false);
    }

    @Override
    public JSONObject query(String type, int read) {
        JSONObject user = userHelper.sign();
        String userId = user.getString("id");
        int pageSize = pagination.getPageSize(20);

        return cache.computeIfAbsent(getCacheKey(userId, type, read, pageSize, pagination.getPageNum()), key -> {
            NoticeModel last = noticeDao.find(userId, 1);
            noticeDao.query(ALL_USER, type, last == null ? dateTime.toTime(user.getString("register")) : last.getTime())
                    .getList().forEach(notice -> {
                notice.setId(null);
                notice.setUser(userId);
                notice.setMarker(1);
                noticeDao.save(notice);
            });

            return noticeDao.query(userHelper.id(), type, read, pagination.getPageSize(20), pagination.getPageNum()).toJson();
        }, false);
    }

    private String getCacheKey(String user, String type, int read, int pageSize, int pageNum) {
        return NoticeModel.NAME + ":" + random.computeIfAbsent(user, key -> generator.random(32))
                + ":" + type + ":" + read + ":" + pageSize + ":" + pageNum;
    }

    @Override
    public JSONObject query(String type, String subject, String[] time) {
        return noticeDao.query(ALL_USER, type, subject, -1, dateTime.toTimeRange(time),
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void send(String type, String subject, String content, String link) {
        send("", type, subject, content, link);
    }

    @Override
    public void send(String user, String type, String subject, String content, String link) {
        NoticeModel notice = new NoticeModel();
        notice.setUser(user);
        notice.setType(type);
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setLink(link);
        notice.setTime(dateTime.now());
        noticeDao.save(notice);
        if (user.equals(ALL_USER))
            random.clear();
        else
            random.remove(user);
    }

    @Override
    public void send(String[] users, String type, String subject, String content, String link) {
        for (String user : users)
            if (!validator.isEmpty(user) && userHelper.exists(user))
                send(user, type, subject, content, link);
    }

    @Override
    public void read(String id) {
        NoticeModel notice = noticeDao.findById(id);
        if (notice == null)
            return;

        notice.setRead(1);
        noticeDao.save(notice);
        random.remove(notice.getUser());
    }

    @Override
    public void reads(String type) {
        noticeDao.query(userHelper.id(), type, null, 0, new Timestamp[2], 0, 0).getList().forEach(notice -> {
            notice.setRead(1);
            noticeDao.save(notice);
        });
        random.remove(userHelper.id());
    }
}
