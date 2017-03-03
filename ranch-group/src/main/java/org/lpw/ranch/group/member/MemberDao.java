package org.lpw.ranch.group.member;

/**
 * @author lpw
 */
interface MemberDao {
    MemberModel findById(String id);

    MemberModel find(String group, String user);

    void save(MemberModel member);

    void delete(String id);
}
