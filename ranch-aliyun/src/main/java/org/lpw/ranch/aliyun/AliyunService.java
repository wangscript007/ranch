package org.lpw.ranch.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.IAcsClient;

/**
 * @author lpw
 */
public interface AliyunService {
    /**
     * 检索配置集。
     *
     * @return 配置集。
     */
    JSONObject query();

    /**
     * 保存配置。
     *
     * @param aliyun 配置。
     */
    void save(AliyunModel aliyun);

    /**
     * 删除配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 上传视频文件。
     *
     * @param key      引用key。
     * @param title    标题。
     * @param fileName 文件名。
     * @param url      文件URL。
     * @return 视频ID，上传失败则返回null。
     */
    String uploadVideo(String key, String title, String fileName, String url);

    /**
     * 获取视频播放URL地址。
     *
     * @param key     引用key。
     * @param videoId 视频ID。
     * @return 视频播放URL地址，获取失败则返回null。
     */
    String getVideoUrl(String key, String videoId);
}
