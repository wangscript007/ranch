package org.lpw.ranch.logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 日志服务。
 *
 * @author lpw
 */
public interface LoggerService {
    /**
     * 检索日志集。
     *
     * @param key   键。
     * @param start 开始时间，格式：yyyy-MM-dd HH:mm:ss。为空或格式错误表示不限制。
     * @param end   结束时间，格式：yyyy-MM-dd HH:mm:ss。为空或格式错误表示不限制。
     * @return 日志集。
     */
    JSONObject query(String key, String start, String end);

    /**
     * 创建日志。
     *
     * @param key 键。
     * @param ps  参数集：不超过10个，超过忽视；每个字符串长度不可超过100字符，超过截断。
     */
    void create(String key, String... ps);
}
