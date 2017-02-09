package org.lpw.ranch.user;

import net.sf.json.JSONObject;

/**
 * @author lpw
 */
public interface UserService {
    /**
     * 注册验证器Bean名称。
     */
    String VALIDATOR_SIGN_UP = UserModel.NAME + ".validator.sign-up";
    /**
     * 登入验证器Bean名称。
     */
    String VALIDATOR_SIGN_IN = UserModel.NAME + ".validator.sign-in";
    /**
     * 已登入验证器Bean名称。
     */
    String VALIDATOR_SIGN = UserModel.NAME + ".validator.sign";
    /**
     * 密码验证器Bean名称。
     */
    String VALIDATOR_PASSWORD = UserModel.NAME + ".validator.password";

    /**
     * 注册。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param type     认证类型。
     * @return 注册成功则返回true；否则返回false。
     */
    boolean signUp(String uid, String password, int type);

    /**
     * 登入验证。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param macId    客户端机器码。
     * @param type     认证类型。
     * @return 认证成功则返回true；否则返回false。
     */
    boolean signIn(String uid, String password, String macId, int type);

    /**
     * 获取当前用户登入信息。
     *
     * @return 当前用户登入信息；如果未登入则返回空JSON数据。
     */
    JSONObject sign();

    /**
     * 修改当前用户信息。
     *
     * @param user 用户信息。
     */
    void modify(UserModel user);

    /**
     * 获取用户数据集。
     *
     * @param ids ID集。
     * @return 用户数据集。
     */
    JSONObject get(String[] ids);
}
