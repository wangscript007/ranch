package org.lpw.ranch.notice;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(NoticeModel.NAME + ".service")
public class NoticeServiceImpl implements NoticeService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private NoticeDao noticeDao;

    @Override
    public JSONObject query(String type, int read) {
        return noticeDao.query(userHelper.id(), type, read, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void send(String user, String type, String subject, String content) {
        NoticeModel notice = new NoticeModel();
        notice.setUser(user);
        notice.setType(type);
        notice.setSubject(subject);
        notice.setContent(content);
        notice.setTime(dateTime.now());
        noticeDao.save(notice);
    }

    @Override
    public void send(String[] users, String type, String subject, String content) {
        for (String user : users)
            if (!validator.isEmpty(user) && userHelper.exists(user))
                send(user, type, subject, content);
    }

    @Override
    public void read(String id) {
        NoticeModel notice = noticeDao.findById(id);
        if (notice == null || !notice.getUser().equals(userHelper.id()))
            return;

        notice.setRead(1);
        noticeDao.save(notice);
    }

    @Override
    public void reads(String type) {
        noticeDao.query(userHelper.id(), type, 0, 0, 0).getList().forEach(notice -> {
            notice.setRead(1);
            noticeDao.save(notice);
        });
    }
}
