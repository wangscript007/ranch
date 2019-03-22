package org.lpw.ranch.editor.screenshot.helper;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface ScreenshotHelper {
    /**
     * 检索元素集。
     *
     * @param editor 编辑器ID值。
     * @return 截图集。
     */
    JSONArray query(String editor);

    /**
     * 截图集。
     *
     * @param editor 编辑器ID值。
     * @return 异步ID。
     */
    String images(String editor);
}
