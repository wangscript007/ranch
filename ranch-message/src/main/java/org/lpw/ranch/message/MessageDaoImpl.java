package org.lpw.ranch.message;

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

    @Override
    public void save(MessageModel message) {
        liteOrm.save(message);
    }
}
