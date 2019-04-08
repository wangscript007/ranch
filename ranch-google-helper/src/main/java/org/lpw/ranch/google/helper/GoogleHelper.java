package org.lpw.ranch.google.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface GoogleHelper {
    /**
     * 认证。
     *
     * @param key   引用key。
     * @param token Token。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String token);
}
