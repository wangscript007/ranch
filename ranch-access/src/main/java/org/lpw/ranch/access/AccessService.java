package org.lpw.ranch.access;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface AccessService {
    /**
     * 检索。
     *
     * @param host      站点。
     * @param uri       请求URI。
     * @param user      用户ID或UID。
     * @param userAgent User Agent，模糊匹配。
     * @param start     开始时间，格式：yyyy-MM-dd。
     * @param end       结束时间，格式：yyyy-MM-dd。
     * @return
     */
    JSONObject query(String host, String uri, String user, String userAgent, String start, String end);

    /**
     * 保存。
     *
     * @param host      站点。
     * @param uri       请求URI。
     * @param ip        IP。
     * @param userAgent User Agent。
     * @param referer   引用。
     * @param header    头信息集。
     */
    void save(String host, String uri, String ip, String userAgent, String referer, Map<String, String> header);
}
