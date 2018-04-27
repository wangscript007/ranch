package org.lpw.ranch.weixin.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Coder;
import org.lpw.tephra.util.Numeric;
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
    private Numeric numeric;
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
    public JSONObject auth(String key, String code, int type) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("key", key);
        parameter.put("code", code);
        parameter.put("type", numeric.toString(type, "0"));

        return carousel.service(this.key + ".auth", null, parameter, false, JSONObject.class);
    }

    @Override
    public String getId(JSONObject object) {
        return object == null ? null : object.getString(object.containsKey("unionid") ? "unionid" : "openid");
    }

    @Override
    public JSONObject decryptAesCbcPkcs7(String iv, String message) {
        Map<String, String> parameter = new HashMap<>();
        parameter.put("iv", iv);
        parameter.put("message", message);

        return carousel.service(this.key + ".decrypt-aes-cbc-pkcs7", null, parameter, false, JSONObject.class);
    }
}
