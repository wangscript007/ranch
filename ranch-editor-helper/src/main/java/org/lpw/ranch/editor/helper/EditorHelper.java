package org.lpw.ranch.editor.helper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface EditorHelper {
    /**
     * 查找编辑器信息。
     *
     * @param id ID值。
     * @return 编辑器信息；如果不存在则返回空JSON{}。
     */
    JSONObject find(String id);

    /**
     * 查找编辑器信息。
     *
     * @param header 请求Header信息集。
     * @param id     ID值。
     * @return 编辑器信息；如果不存在则返回空JSON{}。
     */
    JSONObject find(Map<String, String> header, String id);

    /**
     * 保存编辑器信息。
     *
     * @param id      ID值。
     * @param type    类型。
     * @param name    名称。
     * @param keyword 关键词。
     * @param width   宽度。
     * @param height  高度。
     * @param image   预览图。
     * @param map     扩展属性集。
     * @return 编辑器信息；保存失败则返回空JSON{}。
     */
    JSONObject save(String id, String type, String name, String keyword, int width, int height, String image, Map<String, String> map);

    /**
     * 保存编辑器信息。
     *
     * @param header  请求Header信息集。
     * @param id      ID值。
     * @param type    类型。
     * @param name    名称。
     * @param keyword 关键词。
     * @param width   宽度。
     * @param height  高度。
     * @param image   预览图。
     * @param map     扩展属性集。
     * @return 编辑器信息；保存失败则返回空JSON{}。
     */
    JSONObject save(Map<String, String> header, String id, String type, String name, String keyword, int width, int height,
                    String image, Map<String, String> map);

    /**
     * 保存编辑器信息。
     *
     * @param object 编辑器信息。
     * @return 编辑器信息；保存失败则返回空JSON{}。
     */
    JSONObject save(JSONObject object);

    /**
     * 保存编辑器信息。
     *
     * @param header 请求Header信息集。
     * @param object 编辑器信息。
     * @return 编辑器信息；保存失败则返回空JSON{}。
     */
    JSONObject save(Map<String, String> header, JSONObject object);
}
