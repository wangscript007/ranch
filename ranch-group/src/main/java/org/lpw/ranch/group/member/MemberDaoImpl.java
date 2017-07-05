package org.lpw.ranch.group.member;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(MemberModel.NAME + ".dao")
class MemberDaoImpl implements MemberDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public MemberModel findById(String id) {
        return liteOrm.findById(MemberModel.class, id);
    }

    @Override
    public MemberModel find(String group, String user) {
        return liteOrm.findOne(new LiteQuery(MemberModel.class).where("c_group=? and c_user=?"), new Object[]{group, user});
    }

    @Override
    public PageList<MemberModel> queryByGroup(String group, int type) {
        return query("group", group, type);
    }

    @Override
    public PageList<MemberModel> queryByUser(String user) {
        return query("user", user, 1);
    }

    @Override
    public PageList<MemberModel> queryManager(String group) {
        return query("group", group, 2);
    }

    private PageList<MemberModel> query(String column, String value, int type) {
        return liteOrm.query(new LiteQuery(MemberModel.class).where("c_" + column + "=? and c_type>=?"), new Object[]{value, type});
    }

    @Override
    public void save(MemberModel member) {
        liteOrm.save(member);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(MemberModel.class, id);
    }

    @Override
    public void deleteByGroup(String group) {
        liteOrm.delete(new LiteQuery(MemberModel.class).where("c_group=?"), new Object[]{group});
    }
}
