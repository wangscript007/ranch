package org.lpw.ranch.milestone.helper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface MilestoneHelper {
    /**
     * 查找或新建里程碑。
     *
     * @param user 用户。
     * @param type 类型。
     * @param map  扩展属性集。
     * @return 里程碑。
     */
    JSONObject findSave(String user, String type, Map<String, String> map);
}
