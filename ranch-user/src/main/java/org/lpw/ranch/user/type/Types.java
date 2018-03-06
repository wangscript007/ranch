package org.lpw.ranch.user.type;

import org.lpw.ranch.user.UserModel;

/**
 * 认证类型处理器。
 *
 * @author lpw
 */
public interface Types {
    /**
     * 绑定。
     */
    int BIND = 0;
    /**
     * 自有。
     */
    int SELF = 1;
    /**
     * 微信。
     */
    int WEIXIN = 2;
    /**
     * 类型最大值。
     */
    int MAX = 2;

    /**
     * 获取UID。
     *
     * @param uid      UID。
     * @param password 密码。
     * @param type     类型。
     * @return UID，如果获取失败则返回null。
     */
    String getUid(String uid, String password, int type);

    /**
     * 注册。
     *
     * @param user     用户。
     * @param uid      UID。
     * @param password 密码。
     * @param type     类型。
     */
    void signUp(UserModel user, String uid, String password, int type);
}
