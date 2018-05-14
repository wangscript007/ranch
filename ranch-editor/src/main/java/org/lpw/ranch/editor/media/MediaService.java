package org.lpw.ranch.editor.media;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MediaService {
    /**
     * 是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = MediaModel.NAME + ".validator.exists";

    /**
     * 检索。
     *
     * @param editor 编辑器，为空则检索当前用户资源。
     * @param type   类型：-1：全部；0-背景；1-图片；2-音频；3-视频
     * @return 媒体集。
     */
    JSONObject query(String editor, int type);

    /**
     * 查找媒体信息。
     *
     * @param id ID值。
     * @return 媒体信息；不存在则返回null。
     */
    MediaModel findById(String id);

    /**
     * 保存。
     *
     * @param editor 编辑器。
     * @param type   类型：0-背景；1-图片；2-音频；3-视频。
     * @param url    URL地址。
     * @param name   文件名。
     */
    void save(String editor, int type, String url, String name);

    /**
     * 修改名称。
     *
     * @param id   ID值。
     * @param name 名称。
     * @return 媒体信息。
     */
    JSONObject name(String id, String name);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);
}
