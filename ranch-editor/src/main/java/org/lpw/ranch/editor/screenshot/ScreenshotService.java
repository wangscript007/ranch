package org.lpw.ranch.editor.screenshot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.element.ElementModel;

import java.io.File;
import java.util.List;

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
     * @param sid      Session ID。
     * @param editor   编辑器。
     * @param elements 根元素集。
     * @param nomark   无水印。
     * @param save     保存。
     * @return 文件集。
     */
    List<File> capture(String sid, EditorModel editor, List<ElementModel> elements, boolean nomark, boolean save);

    /**
     * 截图。
     *
     * @param sid    Session ID。
     * @param editor 编辑器。
     * @param page   页面。
     * @param width  宽。
     * @param height 高。
     * @return 图片文件；截图失败返回null。
     */
    File capture(String sid, String editor, String page, int width, int height);

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
     * 设置URI。
     *
     * @param id  ID值。
     * @param uri URI。
     */
    void uri(String id, String uri);

    /**
     * 删除。
     *
     * @param editor 编辑器ID值。
     */
    void delete(String editor);
}
