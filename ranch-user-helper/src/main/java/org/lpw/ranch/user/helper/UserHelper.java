package org.lpw.ranch.user.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 用户服务支持。
 *
 * @author lpw
 */
public interface UserHelper {
    /**
     * 用户是否已登入验证器Bean名称。
     * 默认错误信息key=ranch.user.helper.need-sign-in。
     */
    String VALIDATOR_SIGN_IN = "ranch.user.helper.validator.sign-in";

    /**
     * 是否为ID值或用户是否已登入验证器Bean名称。
     * 默认错误信息key=ranch.user.helper.not-id-and-sign-in。
     */
    String VALIDATOR_ID_OR_SIGN_IN = "ranch.user.helper.validator.id-or-sign-in";

    /**
     * 用户是否存在验证器Bean名称。
     * 默认错误信息key=ranch.user.helper.not-exists。
     */
    String VALIDATOR_EXISTS = "ranch.user.helper.validator.exists";

    /**
     * 获取用户信息。
     *
     * @param id ID值。
     * @return JSON数据，如果未找到则返回仅包含id属性的JSON数据。
     */
    JSONObject get(String id);

    /**
     * 根据编码获得用户信息。
     *
     * @param code 唯一编码。
     * @return 用户信息；如果不存在则返回空JSON。
     */
    JSONObject findByCode(String code);

    /**
     * 根据UID获取用户信息。
     *
     * @param uid UID值。
     * @return 用户信息；如果不存在则返回空JSON对象。
     */
    JSONObject findByUid(String uid);

    /**
     * 根据UID获取用户ID值。
     *
     * @param uid UID值。
     * @return 用户ID值；如果不存在则返回null。
     */
    String findIdByUid(String uid);

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

    /**
     * 获取当前登入用户ID值。
     *
     * @return 当前登入用户ID值；如果未登入则返回null。
     */
    String id();
}
