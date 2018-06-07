package org.lpw.ranch.async;

import com.alibaba.fastjson.JSONObject;

import java.util.concurrent.Callable;

/**
 * @author lpw
 */
public interface AsyncService {
    /**
     * 提交。
     *
     * @param key       引用KEY。
     * @param parameter 参数。
     * @param timeout   超时时长，单位：秒。
     * @param callable  执行处理器。
     * @return 异步ID。
     */
    String submit(String key, String parameter, int timeout, Callable<String> callable);

    /**
     * 保存数据。
     *
     * @param bytes  数据。
     * @param suffix 文件后缀。
     * @return 文件路径。
     */
    String save(byte[] bytes, String suffix);

    /**
     * 获取保存路径。
     *
     * @param suffix 文件后缀。
     * @return 文件路径。
     */
    String newSavePath(String suffix);

    /**
     * 获取根目录。
     *
     * @return 根目录。
     */
    String root();

    /**
     * 查询。
     *
     * @param id ID值。
     * @return 执行状态与结果。
     */
    JSONObject find(String id);
}
