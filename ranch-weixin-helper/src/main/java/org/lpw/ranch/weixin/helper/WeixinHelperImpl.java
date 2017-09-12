package org.lpw.ranch.weixin.helper;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Carousel;
import org.lpw.tephra.util.Converter;
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
    private Converter converter;
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
                + "&redirect_uri=" + converter.encodeUrl(redirectUri, null)
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
    public String getId(JSONObject object) {
        return object == null ? null : object.getString(object.containsKey("unionid") ? "unionid" : "openid");
    }
}
