package org.lpw.ranch.user.auth;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface AuthService {
    /**
     * UID不存在验证器Bean名称。
     */
    String VALIDATOR_UID_NOT_EXISTS = AuthModel.NAME + ".validator.uid.not-exists";

    /**
     * 检索用户的认证信息集。
     *
     * @param user 用户ID。
     * @return 认证信息集，如果不存在则返回空JSON数组。
     */
    JSONArray query(String user);

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
     * 绑定ID。
     *
     * @param user 用户ID。
     * @param id   绑定ID。
     */
    void bind(String user, String id);

    /**
     * 解除绑定。
     *
     * @param id 绑定ID。
     */
    void unbind(String id);
}
