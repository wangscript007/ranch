package org.lpw.ranch.editor.screenshot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ScreenshotService {
    /**
     * 检索截图集。
     *
     * @param editor 编辑器ID值。
     * @return 截图集；不存在则返回空集。
     */
    JSONArray query(String editor);

    /**
     * 查找截图信息。
     *
     * @param editor 编辑器ID值。
     * @param page   页面。
     * @return 截图信息；不存在则返回空JSON。
     */
    JSONObject find(String editor, String page);

    /**
     * 截图。
     *
     * @param editor     编辑器ID值。
     * @param mainHeight 主页面宽。
     * @param mainWidth  主页面高。
     * @param pageHeight 页面宽。
     * @param pageWidth  页面高。
     * @return 异步ID。
     */
    String capture(String editor, int mainWidth, int mainHeight, int pageWidth, int pageHeight);

    /**
     * 截图。
     *
     * @param editor 编辑器ID值。
     * @return 异步ID。
     */
    String capture(String editor);

    /**
     * 创建。
     *
     * @param editor 编辑器ID值。
     * @param index  序号。
     * @param page   页面。
     * @param uri    资源URI地址。
     */
    void create(String editor, int index, String page, String uri);

    /**
     * 删除。
     *
     * @param editor 编辑器ID值。
     */
    void delete(String editor);
}
