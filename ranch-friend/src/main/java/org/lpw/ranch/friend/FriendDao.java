package org.lpw.ranch.friend;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface FriendDao {
    PageList<FriendModel> query(String owner);

    FriendModel find(String owner, String friend);

    void save(FriendModel friend);
}
