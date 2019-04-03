package org.lpw.ranch.user.online;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserModel;

/**
 * @author lpw
 */
public interface OnlineService {
    /**
     * 检索在线数据集。
     *
     * @param user 用户ID。
     * @param uid  认证ID。
     * @param ip   IP地址。
     * @return 在线数据集。
     */
    JSONObject query(String user, String uid, String ip);

    /**
     * 根据SID获取在线信息。
     *
     * @param sid SID。
     * @return 在线信息。
     */
    OnlineModel findBySid(String sid);

    /**
     * 登入。
     *
     * @param user 用户。
     */
    void signIn(UserModel user);

    /**
     * 判断当前用户是否已登入。
     *
     * @return 如果已登入则返回true；否则返回false。
     */
    boolean isSign();

    /**
     * 登出。
     */
    void signOut();

    /**
     * 强制登出。
     *
     * @param id ID值。
     */
    void signOutId(String id);

    /**
     * 强制登出。
     *
     * @param user 用户ID值。
     */
    void signOutUser(String user);

    /**
     * 统计在线用户数。
     *
     * @return 在线用户数。
     */
    int count();
}
