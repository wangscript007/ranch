package org.lpw.ranch.chat.friend;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface FriendService {
    /**
     * 检索当前用户好友列表。
     *
     * @return 当前用户好友列表；如果未找到则返回空集。
     */
    JSONArray query();

    /**
     * 添加好友。
     *
     * @param friend 好友ID或唯一编码。
     * @param note   请求备注。
     */
    void create(String friend, String note);

    /**
     * 通过好友审核。
     *
     * @param friend 好友ID。
     * @param note   备注。
     */
    void pass(String friend, String note);
}
