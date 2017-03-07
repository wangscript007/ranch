package org.lpw.ranch.friend;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface FriendDao {
    PageList<FriendModel> query(String owner, int state, boolean sortable);

    FriendModel find(String owner, String user);

    void save(FriendModel user);
}
