package org.lpw.ranch.user;

import net.sf.json.JSONObject;

/**
 * @author lpw
 */
public interface UserService {
    /**
     * 登入验证器Bean名称。
     */
    String VALIDATOR_SIGN_IN = UserModel.NAME + ".validator.sign-in";

    /**
     * 登入验证。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param macId    客户端机器码。
     * @return 认证成功则返回true；否则返回false。
     */
    boolean signIn(String uid, String password, String macId);

    /**
     * 获取当前用户登入信息。
     *
     * @return 当前用户登入信息；如果未登入则返回空JSON数据。
     */
    JSONObject sign();
}
