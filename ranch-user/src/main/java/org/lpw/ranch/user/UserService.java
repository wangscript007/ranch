package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface UserService {
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
     * 邀请人。
     *
     * @param code 邀请人code，不为空则设置。
     * @return 邀请人code。
     */
    String inviter(String code);

    /**
     * 注册。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param type     认证类型。
     */
    void signUp(String uid, String password, int type);

    /**
     * 登入验证。
     *
     * @param uid      UID值。
     * @param password 密码。
     * @param type     认证类型。
     * @return 认证成功则返回true；否则返回false。
     */
    boolean signIn(String uid, String password, int type);

    /**
     * 获取微信PC端登入跳转URL地址。
     *
     * @param key         微信引用KEY。
     * @param redirectUrl 返回跳转URL地址。
     * @return 微信PC端登入跳转URL地址。
     */
    String signInWxPc(String key, String redirectUrl);

    /**
     * 验证微信PC端登入回调。
     *
     * @param code 认证code。
     * @return 跳转地址。
     */
    String signInWxPcRedirect(String code);

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
     * 加密密码。
     *
     * @param password 密码明文。
     * @return 加密后的密码。
     */
    String password(String password);

    /**
     * 获取当前Session中用户对象。
     *
     * @return 用户对象；不存在则返回null。
     */
    UserModel fromSession();

    /**
     * 获取当前用户登入UID。
     *
     * @return 当前用户登入UID。
     */
    String uidFromSession();

    /**
     * 获取用户数据。
     *
     * @param id ID值。
     * @return 用户数据；不存在则返回null。
     */
    UserModel findById(String id);

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
     * @param uid UID值。
     * @return 用户数据；不存在则返回空JSON。
     */
    JSONObject findByUid(String uid);

    /**
     * 查找用户或当前用户。
     *
     * @param idUidCode 用户ID或UID或code值。
     * @return 用户信息，不存在且未登入则返回空JSON{}。
     */
    JSONObject findOrSign(String idUidCode);

    /**
     * 检索用户信息集。
     *
     * @param uid      UID。
     * @param idcard   身份证号；为空则表示所有。
     * @param name     姓名；为空则表示所有。
     * @param nick     昵称；为空则表示所有。
     * @param mobile   用户手机号；为空则表示所有。
     * @param email    Email地址；为空则表示所有。
     * @param code     唯一编码；为空则表示所有。
     * @param minGrade 最小等级，-1表示不限制。
     * @param maxGrade 最大等级，-1表示不限制。
     * @param state    状态：-1-所有；0-正常；1-禁用。
     * @param register 注册日期范围，格式：yyyy-MM-dd；为空表示不限制。
     * @return 用户信息集。
     */
    JSONObject query(String uid, String idcard, String name, String nick, String mobile, String email, String code,
                     int minGrade, int maxGrade, int state, String[] register);

    /**
     * 更新用户信息。
     *
     * @param user 用户信息。
     */
    void update(UserModel user);

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

    /**
     * 清空当前用户缓存数据。
     */
    void clearCache();
}
