package org.lpw.ranch.weixin.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Coder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.weixin.helper")
public class WeixinHelperImpl implements WeixinHelper {
    @Inject
    private Coder coder;
    @Inject
    private Carousel carousel;
    @Value("${ranch.weixin.key:ranch.weixin}")
    private String key;

    @Override
    public String getAppId(String key) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);

        return carousel.service(this.key + ".app-id", null, parameter, false).getString("data");
    }

    @Override
    public String getPcSignInUrl(String key, String redirectUri) {
        return "https://open.weixin.qq.com/connect/qrconnect?appid=" + getAppId(key)
                + "&redirect_uri=" + coder.encodeUrl(redirectUri, null)
                + "&response_type=code&scope=snsapi_login#wechat_redirect";
    }

    @Override
    public JSONObject auth(String key, String code) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("code", code);

        return carousel.service(this.key + ".auth", null, parameter, false, JSONObject.class);
    }

    @Override
    public JSONObject auth(String key, String code, String iv, String message, String iv2, String message2) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("code", code);
        parameter.put("iv", iv);
        parameter.put("message", message);
        parameter.put("iv2", iv2);
        parameter.put("message2", message2);

        return carousel.service(this.key + ".auth-mini", null, parameter, false, JSONObject.class);
    }

    @Override
    public JSONObject decryptAesCbcPkcs7(String iv, String message) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("iv", iv);
        parameter.put("message", message);

        return carousel.service(this.key + ".decrypt-aes-cbc-pkcs7", null, parameter, false, JSONObject.class);
    }

    @Override
    public JSONObject sendTemplateMessage(String key, String receiver, String templateId,
                                          String url, String miniAppId, String miniPagePath, JSONObject data, String color) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("receiver", receiver);
        parameter.put("templateId", templateId);
        parameter.put("url", url);
        parameter.put("miniAppId", miniAppId);
        parameter.put("miniPagePath", miniPagePath);
        parameter.put("data", data.toJSONString());
        parameter.put("color", color);

        return carousel.service(this.key + ".send-template-message", null, parameter, false, JSONObject.class);
    }

    @Override
    public JSONObject sendMiniTemplateMessage(String key, String receiver, String templateId, String page, String formId,
                                              JSONObject data, String keyword) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("receiver", receiver);
        parameter.put("templateId", templateId);
        parameter.put("page", page);
        parameter.put("formId", formId);
        parameter.put("data", data.toJSONString());
        parameter.put("keyword", keyword);

        return carousel.service(this.key + ".send-mini-template-message", null, parameter, false, JSONObject.class);
    }
}
