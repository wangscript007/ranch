package org.lpw.ranch.form;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface FormService {
    /**
     * 保存。
     *
     * @param id    ID值：不存在则新增；存在则修改。
     * @param name  名称。
     * @param time  时间轴。
     * @param state 状态。
     * @return 表单信息。
     */
    JSONObject save(String id, String name, String time, int state);
}
