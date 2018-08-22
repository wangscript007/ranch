package org.lpw.ranch.user.auth;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface AuthService {
    /**
     * UID不存在验证器Bean名称。
     * 默认错误信息key=ranch.user.auth.uid.exists。
     */
    String VALIDATOR_UID_NOT_EXISTS = AuthModel.NAME + ".validator.uid.not-exists";
    /**
     * UID存在验证器Bean名称。
     * 默认错误信息key=ranch.user.auth.uid.not-exists。
     */
    String VALIDATOR_UID_EXISTS = AuthModel.NAME + ".validator.uid.exists";

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
     * @param userId   用户ID。
     * @param uid      UID值。
     * @param type     认证类型。
     * @param nick     第三方账号昵称。
     * @param portrait 第三方头像URL。
     * @return 认证信息。
     */
    AuthModel create(String userId, String uid, int type, String nick, String portrait);

    /**
     * 根据uid检索认证信息。
     *
     * @param uid UID值。
     * @return 认证信息，如果不存在则返回null。
     */
    AuthModel findByUid(String uid);

    /**
     * 删除当前认证。
     */
    void delete();
}
