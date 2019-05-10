package org.lpw.ranch.weixin.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * 微信支持。
 *
 * @author lpw
 */
public interface WeixinHelper {
    /**
     * 配置KEY是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = "ranch.weixin.helper.valiator.exists";

    /**
     * 获取APP ID。
     *
     * @param key 配置KEY。
     * @return APP ID，如果不存在则返回null。
     */
    String getAppId(String key);

    /**
     * 获取PC端登入微信请求URL。
     *
     * @param key         配置KEY。
     * @param redirectUri 授权回调URL。
     * @return 请求URL。
     */
    String getPcSignInUrl(String key, String redirectUri);

    /**
     * 认证用户信息。
     *
     * @param key  配置KEY。
     * @param code 微信认证code。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code);

    /**
     * 认证小程序用户信息。
     *
     * @param key     配置key。
     * @param code    微信认证code。
     * @param iv      加密算法的初始向量。
     * @param message 加密数据。
     * @return 如果认证通过则返回用户授权信息，否则返回空JSON。
     */
    JSONObject auth(String key, String code, String iv, String message);

    /**
     * 获取微信用户ID。优先使用unionid，如果不存在则返回openid。
     *
     * @param object 用户授权信息
     * @return 如果存在则返回ID，否则返回null。
     */
    String getId(JSONObject object);

    /**
     * 解密AES-128-CBC/PKCS#7数据。
     *
     * @param iv      初始化参数。
     * @param message 加密数据。
     * @return 解密后的数据，如果解密失败则返回空JSON{}。
     */
    JSONObject decryptAesCbcPkcs7(String iv, String message);

    /**
     * 发送模板消息。
     *
     * @param key          引用key。
     * @param receiver     接受者。
     * @param templateId   模板ID。
     * @param url          跳转URL。
     * @param miniAppId    小程序APP ID。
     * @param miniPagePath 小程序页面路径。
     * @param data         模板数据。
     * @param color        颜色。
     * @return 发送结果。
     */
    JSONObject sendTemplateMessage(String key, String appId, String receiver, String templateId,
                                   String url, String miniAppId, String miniPagePath, JSONObject data, String color);
}
