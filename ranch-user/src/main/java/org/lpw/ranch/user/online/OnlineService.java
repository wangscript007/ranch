package org.lpw.ranch.user.online;

import com.alibaba.fastjson.JSONObject;

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
     * 登入。
     *
     * @param user 用户ID。
     */
    void signIn(String user);

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
     * 登出。
     *
     * @param user 用户ID。
     * @param uid  认证ID。
     * @param ip   IP地址。
     */
    void signOut(String user, String uid, String ip);
}
