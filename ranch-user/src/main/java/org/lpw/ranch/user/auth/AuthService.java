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
}
