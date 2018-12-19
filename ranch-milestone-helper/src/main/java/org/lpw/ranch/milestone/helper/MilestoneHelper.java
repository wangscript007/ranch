package org.lpw.ranch.milestone.helper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface MilestoneHelper {
    /**
     * 查找。
     *
     * @param type 类型。
     * @return 里程碑。
     */
    JSONObject find(String type);

    /**
     * 新增。
     *
     * @param user 用户。
     * @param type 类型。
     * @param map  扩展属性集。
     * @return 里程碑。
     */
    JSONObject create(String user, String type, Map<String, String> map);
}
