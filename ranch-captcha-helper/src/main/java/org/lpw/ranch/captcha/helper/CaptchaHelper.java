package org.lpw.ranch.captcha.helper;

/**
 * @author lpw
 */
public interface CaptchaHelper {
    /**
     * 校验验证器Bean名称。
     * 默认错误信息key=ranch.captcha.helper.validate.failure。
     */
    String VALIDATOR_VALIDATE="ranch.captcha.helper.validator.validate";

    /**
     * 检验验证码。
     *
     * @param key  引用key。
     * @param code 验证码。
     * @return 如果验证通过则返回true；否则返回false。
     */
    boolean validate(String key, String code);
}
