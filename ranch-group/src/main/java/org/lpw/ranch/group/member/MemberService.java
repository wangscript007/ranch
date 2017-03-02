package org.lpw.ranch.group.member;

/**
 * @author lpw
 */
public interface MemberService {
    /**
     * 类型。
     */
    enum Type {
        /**
         * 0-待审核
         */
        New,
        /**
         * 1-普通成员
         */
        Normal,
        /**
         * 2-管理员
         */
        Manager,
        /**
         * 3-所有者
         */
        Owner
    }

    /**
     * 创建所有者成员。
     *
     * @param group 组ID。
     * @param owner 所有者ID。
     */
    void create(String group, String owner);

    /**
     * 加入群组。
     *
     * @param group  群组ID。
     * @param reason 申请加入理由。
     */
    void join(String group, String reason);
}
