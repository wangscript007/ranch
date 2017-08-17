package org.lpw.ranch.dbtool.table;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface TableService {
    /**
     * 检索表信息集。
     *
     * @param schema Schema ID值。
     * @param group  分组ID值。
     * @param name   表名称，模糊匹配。
     * @return 表信息集。
     */
    JSONArray query(String schema, String group, String name);

    /**
     * 保存表信息。
     *
     * @param table 表信息。
     * @return 表信息。
     */
    JSONObject save(TableModel table);

    /**
     * 删除表信息。
     *
     * @param id ID值。
     */
    void delete(String id);
}
