package org.lpw.ranch.message;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author lpw
 */
interface MessageDao {
    void save(MessageModel message);

    PageList<MessageModel> query(Timestamp time, String sender, Set<String> receivers, int size);

    MessageModel findByCode(String code);
}
