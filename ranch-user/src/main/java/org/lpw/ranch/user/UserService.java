package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface UserService {
    /**
     * 认证类型。
     */
    enum Type {
        /**
         * 绑定ID。
         */
        Bind,
        /**
         * 自有账号。
         */
        Self,
        /**
         * 微信。
         */
        WeiXin
    }

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
     * 用户信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = UserModel.NAME + ".validator.exists";

    /**
     * 注册。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param type     认证类型。
     */
    void signUp(String uid, String password, Type type);

    /**
     * 登入验证。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param macId    客户端机器码。
     * @param type     认证类型。
     * @return 认证成功则返回true；否则返回false。
     */
    boolean signIn(String uid, String password, String macId, Type type);

    /**
     * 获取当前用户登入信息。
     *
     * @return 当前用户登入信息；如果未登入则返回空JSON数据。
     */
    JSONObject sign();

    /**
     * 登出。
     */
    void signOut();

    /**
     * 登出。
     *
     * @param sid Session ID。
     */
    void signOut(String sid);

    /**
     * 修改当前用户信息。
     *
     * @param user 用户信息。
     */
    void modify(UserModel user);

    /**
     * 修改密码。
     *
     * @param oldPassword 旧密码。
     * @param newPassword 新密码。
     * @return 如果修改成功则返回true；否则返回false。
     */
    boolean password(String oldPassword, String newPassword);

    /**
     * 设置当前用户头像。
     *
     * @param uri 头像URI地址。
     */
    void portrait(String uri);

    /**
     * 获取用户数据集。
     *
     * @param ids ID集。
     * @return 用户数据集。
     */
    JSONObject get(String[] ids);

    /**
     * 根据编码获得用户信息。
     *
     * @param code 唯一编码。
     * @return 用户信息；如果不存在则返回空JSON。
     */
    JSONObject findByCode(String code);

    /**
     * 获取用户数据。
     *
     * @param id ID值。
     * @return 用户数据；不存在则返回null。
     */
    UserModel findById(String id);

    /**
     * 获取用户数据。
     *
     * @param uid UID值。
     * @return 用户数据；不存在则返回空JSON。
     */
    JSONObject findByUid(String uid);

    /**
     * 检索用户信息集。
     *
     * @param mobile 用户手机号；为空则表示所有。
     * @return 用户信息集。
     */
    JSONObject query(String mobile);

    /**
     * 设置用户等级。
     *
     * @param id    用户ID值。
     * @param grade 等级值。
     */
    void grade(String id, int grade);

    /**
     * 设置用户状态。
     *
     * @param id    用户ID值。
     * @param state 状态值。
     */
    void state(String id, int state);
}
