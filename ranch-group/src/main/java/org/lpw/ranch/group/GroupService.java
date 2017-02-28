package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface GroupService {
    /**
     * 创建新群组。
     *
     * @param name  名称。
     * @param note  公告。
     * @param audit 新成员是否需要审核：0-否；1-是。
     * @return 新群组信息。
     */
    JSONObject create(String name, String note, int audit);
}
