package org.lpw.ranch.chat.message;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(MessageModel.NAME + ".dao")
class MessageDaoImpl implements MessageDao {
    @Inject
    private LiteOrm liteOrm;
}
