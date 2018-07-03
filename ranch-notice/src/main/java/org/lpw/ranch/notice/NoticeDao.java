package org.lpw.ranch.notice;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface NoticeDao {
    PageList<NoticeModel> query(String user, String type, int read, int pageSize, int pageNum);

    NoticeModel findById(String id);

    void save(NoticeModel notice);
}
