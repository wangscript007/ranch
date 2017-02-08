package org.lpw.ranch.user.auth;

/**
 * @author lpw
 */
public interface AuthService {
    /**
     * 创建新认证。
     *
     * @param userId 用户ID。
     * @param uid    UID值。
     * @param type   认证类型。
     * @return 认证信息。
     */
    AuthModel create(String userId, String uid, int type);

    /**
     * 根据uid检索认证信息。
     *
     * @param uid UID值。
     * @return 认证信息，如果不存在则返回null。
     */
    AuthModel findByUid(String uid);

    /**
     * 绑定MAC ID。
     *
     * @param userId 用户ID。
     * @param macId  MAC ID。
     */
    void bindMacId(String userId, String macId);
}
