package org.lpw.ranch.facebook.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface FacebookHelper {
    /**
     * 认证。
     *
     * @param key  引用key。
     * @param code 认真code。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code);
}
