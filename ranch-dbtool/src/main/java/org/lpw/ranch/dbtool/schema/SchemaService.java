package org.lpw.ranch.dbtool.schema;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface SchemaService {
    /**
     * 数据源配置是否存在验证器Bean名称。
     * 默认错误信息key=ranch.dbtool.schema.not-exists。
     */
    String VALIDATOR_EXISTS = SchemaModel.NAME + ".validator.exists";

    /**
     * 数据源配置key是否不存在验证器Bean名称。
     * 默认错误信息key=ranch.dbtool.schema.key.exists。
     */
    String VALIDATOR_KEY_NOT_EXISTS = SchemaModel.NAME + ".validator.key.not-exists";

    /**
     * 类型是否合法验证器Bean名称。
     * 默认错误信息key=ranch.dbtool.schema.illegal-type。
     */
    String VALIDATOR_TYPE = SchemaModel.NAME + ".validator.type";

    /**
     * 检索数据源配置集。
     *
     * @param group 分组ID。
     * @param key   KEY，模糊匹配。
     * @param type  数据源类型。
     * @param ip    IP地址，模糊匹配。
     * @param name  数据库名称，模糊匹配。
     * @return 数据源配置集；未检索到则返回空JSON数组。
     */
    JSONArray query(String group, String key, String type, String ip, String name);

    /**
     * 检索数据源配置。
     *
     * @param id ID值。
     * @return 数据源配置；如果不存在则返回null。
     */
    SchemaModel findById(String id);

    /**
     * 检索数据源配置。
     *
     * @param key key。
     * @return 数据源配置；如果不存在则返回null。
     */
    SchemaModel findByKey(String key);

    /**
     * 保存数据源配置。
     *
     * @param model 数据源配置。
     * @return 数据源配置。
     */
    JSONObject save(SchemaModel model);

    /**
     * 删除数据源配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 设置表数量。
     *
     * @param id     ID值。
     * @param tables 表数量。
     */
    void setTables(String id, int tables);
}
