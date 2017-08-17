package org.lpw.ranch.dbtool.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ColumnService {
    /**
     * 检索字段集。
     *
     * @param table 表ID值。
     * @param name  名称，模糊匹配。
     * @return 字段集。
     */
    JSONArray query(String table, String name);

    /**
     * 保存字段信息。
     *
     * @param column 字段信息。
     * @return 字段信息。
     */
    JSONObject save(ColumnModel column);
}
