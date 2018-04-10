package org.lpw.ranch.editor.helper;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface EditorHelper {
    /**
     * 保存编辑器信息。
     *
     * @param type    类型。
     * @param name    名称。
     * @param keyword 关键词。
     * @param width   宽度。
     * @param height  高度。
     * @param image   预览图。
     * @param map     扩展属性集。
     * @return 编辑器信息。
     */
    JSONObject save(String type, String name, String keyword, int width, int height, String image, Map<String, String> map);
}
