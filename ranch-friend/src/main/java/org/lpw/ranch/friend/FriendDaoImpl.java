package org.lpw.ranch.friend;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(FriendModel.NAME + ".dao")
class FriendDaoImpl implements FriendDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<FriendModel> query(String owner, int state, boolean sortable) {
        LiteQuery query = new LiteQuery(FriendModel.class).where("c_owner=? and c_state=?");
        if (sortable)
            query.order("c_create desc");

        return liteOrm.query(query, new Object[]{owner, state});
    }

    @Override
    public FriendModel find(String owner, String user) {
        return liteOrm.findOne(new LiteQuery(FriendModel.class).where("c_owner=? and c_user=?"), new Object[]{owner, user});
    }

    @Override
    public void save(FriendModel user) {
        liteOrm.save(user);
    }
}
