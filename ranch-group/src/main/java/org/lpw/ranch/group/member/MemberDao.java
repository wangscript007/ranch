package org.lpw.ranch.group.member;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface MemberDao {
    MemberModel findById(String id);

    MemberModel find(String group, String user);

    PageList<MemberModel> queryByGroup(String group, int type);

    PageList<MemberModel> queryByUser(String user);

    PageList<MemberModel> queryManager(String group);

    void save(MemberModel member);

    void delete(String id);

    void deleteByGroup(String group);
}
