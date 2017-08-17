package org.lpw.ranch.captcha;

import com.octo.captcha.engine.CaptchaEngine;

/**
 * @author lpw
 */
public interface EngineFactory {
    /**
     * 创建新验证码引擎。
     *
     * @param captcha 验证码配置信息。
     * @return 验证码引擎实例。
     */
    CaptchaEngine createCaptchaEngine(CaptchaModel captcha);
}
