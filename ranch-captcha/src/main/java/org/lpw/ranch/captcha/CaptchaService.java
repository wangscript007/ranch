package org.lpw.ranch.captcha;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.OutputStream;

/**
 * @author lpw
 */
public interface CaptchaService {
    /**
     * 检索验证码配置集。
     *
     * @return 验证码配置集。
     */
    JSONArray query();

    /**
     * 保存验证码配置。
     *
     * @param captcha 验证码配置。
     * @return 验证码配置。
     */
    JSONObject save(CaptchaModel captcha);

    /**
     * 删除验证码配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 输出验证码图片。
     *
     * @param key          引用key。
     * @param outputStream 输出流。
     */
    void image(String key, OutputStream outputStream);

    /**
     * 检验验证码。
     *
     * @param key  引用key。
     * @param code 验证码。
     * @return 如果验证通过则返回true；否则返回false。
     */
    boolean validate(String key, String code);
}
