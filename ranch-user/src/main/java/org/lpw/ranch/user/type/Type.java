package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;

/**
 * 认证类型。
 *
 * @author lpw
 */
public interface Type {
    /**
     * 获取类型KEY值。
     *
     * @return 类型KEY值。
     */
    int getKey();

    /**
     * 获取UID。
     *
     * @param uid      UID。
     * @param password 密码。
     * @return UID，如果获取失败则返回null。
     */
    String getUid(String uid, String password);

    /**
     * 注册。
     *
     * @param user     用户。
     * @param uid      UID。
     * @param password 密码。
     */
    void signUp(UserModel user, String uid, String password);

    /**
     * 获取第三方认证昵称。
     *
     * @param uid      UID。
     * @param password 密码。
     * @return 昵称，不存在则返回null。
     */
    String getNick(String uid, String password);

    /**
     * 获取第三方头像URL。
     *
     * @param uid      UID。
     * @param password 密码。
     * @return 头像URL，不存在则返回null。
     */
    String getPortrait(String uid, String password);

    /**
     * 获取第三方认证信息。
     *
     * @param uid      UID。
     * @param password 密码。
     * @return 认证信息，不存在则返回null。
     */
    JSONObject getAuth(String uid, String password);
}
