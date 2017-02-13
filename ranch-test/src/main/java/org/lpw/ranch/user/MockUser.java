package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MockUser {
    /**
     * 注册用户服务。
     */
    void register();

    /**
     * 验证。
     *
     * @param user 用户信息。
     * @param id   目标ID值。
     */
    void verify(JSONObject user, String id);

    /**
     * 设置用户登入信息。
     *
     * @param id 用户ID。
     */
    void sign(String id);
}
