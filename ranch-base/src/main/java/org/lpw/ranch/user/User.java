package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface User {
    /**
     * 用户是否已登入验证器Bean名称。
     */
    String VALIDATOR_SIGN_IN = "ranch.base.user.validator.sign-in";

    /**
     * 获取用户信息。
     *
     * @param id ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject get(String id);

    /**
     * 获取用户登入信息。
     *
     * @return 用户登入信息，如果未登入则返回空JSON对象。
     */
    JSONObject sign();
}
