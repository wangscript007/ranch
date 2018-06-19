package org.lpw.ranch.editor.element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author lpw
 */
public interface ElementService {
    /**
     * 元素信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = ElementModel.NAME + ".validator.exists";
    /**
     * 编辑器是否匹配验证器Bean名称。
     */
    String VALIDATOR_EDITOR = ElementModel.NAME + ".validator.editor";
    /**
     * 修改时间是否匹配验证器Bean名称。
     */
    String VALIDATOR_MODIFY = ElementModel.NAME + ".validator.modify";

    /**
     * 检索元素集。
     *
     * @param editor    编辑器ID值。
     * @param parent    父元素ID值。
     * @param recursive 是否递归获取子元素集。
     * @return 元素集。
     */
    JSONArray query(String editor, String parent, boolean recursive);

    /**
     * 获取根元素集。
     *
     * @param editor 编辑器ID值。
     * @return 元素集。
     */
    List<ElementModel> list(String editor);

    /**
     * 查找元素。
     *
     * @param id        ID值。
     * @param recursive 是否递归获取子元素集。
     * @return 元素。
     */
    JSONObject find(String id, boolean recursive);

    /**
     * 查找元素信息。
     *
     * @param id ID值。
     * @return 元素信息，不存在则返回null。
     */
    ElementModel findById(String id);

    /**
     * 保存元素信息。
     *
     * @param element 元素信息。
     * @return 元素信息。
     */
    JSONObject save(ElementModel element);

    /**
     * 排序。
     *
     * @param editor   编辑器ID值。
     * @param parent   父元素ID值。
     * @param ids      元素ID集。
     * @param modifies 修改时间集。
     * @return 修改数据集。
     */
    JSONArray sort(String editor, String parent, String[] ids, String[] modifies);

    /**
     * 删除元素。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 批量操作。
     *
     * @param parameters 参数集。
     * @return 操作结果集。
     */
    JSONArray batch(String parameters);

    /**
     * 复制。
     *
     * @param source 源编辑器ID值。
     * @param target 目标编辑器ID值。
     */
    void copy(String source, String target);

    /**
     * 附加文本内容。
     *
     * @param editor 编辑器ID值。
     * @param data   目标数据对象。
     */
    void text(String editor, StringBuilder data);
}
