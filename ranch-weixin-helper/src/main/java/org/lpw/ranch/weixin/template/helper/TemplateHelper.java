package org.lpw.ranch.weixin.template.helper;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface TemplateHelper {
    /**
     * 发送。
     *
     * @param key      引用key。
     * @param receiver 接收者。
     * @param formId   表单ID。
     * @param data     参数集。
     * @return 发送结果。
     */
    JSONObject send(String key, String receiver, String formId, JSONObject data);
}
