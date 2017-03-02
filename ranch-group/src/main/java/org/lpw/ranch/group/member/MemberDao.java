package org.lpw.ranch.group.member;

/**
 * @author lpw
 */
interface MemberDao {
    MemberModel find(String group, String user);

    void save(MemberModel member);
}
