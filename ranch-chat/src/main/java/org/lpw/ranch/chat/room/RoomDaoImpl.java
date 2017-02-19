package org.lpw.ranch.chat.room;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(RoomModel.NAME + ".dao")
class RoomDaoImpl implements RoomDao {
    @Inject
    private LiteOrm liteOrm;
}
