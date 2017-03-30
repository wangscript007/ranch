package org.lpw.ranch.group.member;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface MemberDao {
    MemberModel findById(String id);

    MemberModel find(String group, String user);

    PageList<MemberModel> queryByGroup(String group);

    PageList<MemberModel> queryByUser(String user);

    PageList<MemberModel> query(String group, int type);

    void save(MemberModel member);

    void delete(String id);
}
