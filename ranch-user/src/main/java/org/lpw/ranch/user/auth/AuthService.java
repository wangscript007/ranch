package org.lpw.ranch.user.auth;

/**
 * @author lpw
 */
public interface AuthService {
    /**
     * 根据uid检索认证信息。
     *
     * @param uid uid值。
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
