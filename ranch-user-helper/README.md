# 用户支持
```java
package org.lpw.ranch.user;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户服务支持类。
 *
 * @author lpw
 */
public interface UserHelper {
    /**
     * 用户是否已登入验证器Bean名称。
     */
    String VALIDATOR_SIGN_IN = "ranch.user-helper.validator.sign-in";

    /**
     * 获取用户信息。
     *
     * @param id ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject get(String id);

    /**
     * 填充用户信息。
     *
     * @param array 要填充的数据集。
     * @param names 要填充的属性名称集。
     * @return 填充后的数据集。
     */
    JSONArray fill(JSONArray array, String[] names);

    /**
     * 获取用户登入信息。
     *
     * @return 用户登入信息，如果未登入则返回空JSON对象。
     */
    JSONObject sign();
}
```