package org.lpw.ranch.linkedin.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface LinkedinHelper {
    /**
     * 认证。
     *
     * @param key         引用key。
     * @param code        认真code。
     * @param redirectUri 转发请求地址。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code, String redirectUri);
}
