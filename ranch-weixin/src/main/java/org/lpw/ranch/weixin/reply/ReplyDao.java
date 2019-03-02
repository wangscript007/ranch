package org.lpw.ranch.weixin.reply;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface ReplyDao {
    PageList<ReplyModel> query(String key, String receiveType, String receiveMessage, int state, int pageSize, int pageNum);

    ReplyModel findById(String id);

    void save(ReplyModel reply);

    void delete(String id);
}
