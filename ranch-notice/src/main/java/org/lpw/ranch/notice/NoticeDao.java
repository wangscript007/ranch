package org.lpw.ranch.notice;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface NoticeDao {
    PageList<NoticeModel> query(String user, String type, int read, int pageSize, int pageNum);

    PageList<NoticeModel> query(String user, String type, String subject, int read, Timestamp[] time, int pageSize, int pageNum);

    PageList<NoticeModel> query(String user, String type, Timestamp time);

    NoticeModel findById(String id);

    NoticeModel find(String user, int marker);

    void save(NoticeModel notice);
}
