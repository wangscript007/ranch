package org.lpw.ranch.chat.friend;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface FriendDao {
    PageList<FriendModel> query(String owner);

    FriendModel findById(String id);

    void save(FriendModel friend);
}
