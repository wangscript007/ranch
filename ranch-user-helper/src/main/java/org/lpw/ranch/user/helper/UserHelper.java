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
     * 用户是否存在验证器Bean名称。
     * 默认错误信息key=ranch.user.helper.not-exists。
     */
    String VALIDATOR_EXISTS = "ranch.user.helper.validator.exists";

    /**
     * 用户是否存在验证器Bean名称。
     * 默认错误信息key=ranch.user.helper.not-exists-and-not-sign-in。
     */
    String VALIDATOR_EXISTS_OR_SIGN_IN = "ranch.user.helper.validator.exists-or-sign-in";
    /**
     * 用户等级是否介于目标区间验证器Bean名称。
     * 默认错误信息key=ranch.user.helper.grade.illegal。
     */
    String VALIDATOR_GRADE="ranch.user.helper.validator.grade";

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
     * 查找用户或当前用户。
     *
     * @param idUidCode 用户ID或UID或code值。
     * @return 用户信息，不存在且未登入则返回空JSON{}。
     */
    JSONObject findOrSign(String idUidCode);

    /**
     * 根据UID获取用户ID值。
     *
     * @param uid          UID值。
     * @param defaultValue 默认值。
     * @return 用户ID值；如果不存在则返回默认值。
     */
    String findIdByUid(String uid, String defaultValue);

    /**
     * 获取用户的UID集。
     *
     * @param id 用户ID。
     * @return UID集。
     */
    String[] uids(String id);

    /**
     * 判断ID是否存在。
     *
     * @param id ID值。
     * @return 如果存在则返回true；否则返回false。
     */
    boolean exists(String id);

    /**
     * 判断用户是否已登入。
     *
     * @return 如果已登入则返回true；否则返回false。
     */
    boolean signIn();

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
