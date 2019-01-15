package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface EditorService {
    /**
     * 编辑器信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = EditorModel.NAME + ".validator.exists";
    /**
     * 编辑器状态是否可编辑验证器Bean名称。
     */
    String VALIDATOR_EDITABLE = EditorModel.NAME + ".validator.editable";

    /**
     * 检索编辑器信息集。
     *
     * @param user        用户ID。
     * @param uid         UID。
     * @param mobile      用户手机号。
     * @param email       用户Email。
     * @param nick        用户昵称。
     * @param template    模板。
     * @param type        类型。
     * @param name        名称。
     * @param label       关键词。
     * @param modified    最小已修改根节点数。
     * @param states      状态集。
     * @param createStart 创建开始日期，格式：yyyy-MM-dd。
     * @param createEnd   创建结束日期，格式：yyyy-MM-dd。
     * @param modifyStart 编辑开始日期，格式：yyyy-MM-dd。
     * @param modifyEnd   编辑结束日期，格式：yyyy-MM-dd。
     * @param order       排序规则。
     * @return 编辑器信息集。
     */
    JSONObject query(String user, String uid, String mobile, String email, String nick, int template, String type, String name, String label,
                     int modified, String[] states, String createStart, String createEnd, String modifyStart, String modifyEnd, Order order);

    /**
     * 检索当前用户编辑器信息集。
     *
     * @param template 模板。
     * @param type     类型。
     * @param states   状态集。
     * @return 编辑器信息集。
     */
    JSONObject user(int template, String type, String[] states);

    /**
     * 查找编辑器信息。
     *
     * @param id ID值。
     * @return 编辑器信息，不存在则返回null。
     */
    EditorModel findById(String id);

    /**
     * 查找编辑器信息。
     *
     * @param id ID值。
     * @return 编辑器信息。
     */
    JSONObject find(String id);

    /**
     * 保存编辑器信息。
     *
     * @param editor 编辑器信息。
     * @return 保存后的JSON信息。
     */
    JSONObject save(EditorModel editor);

    /**
     * 修改编辑器信息。
     *
     * @param editor   编辑器信息。
     * @param template 模板：-1-不修改；0-否；1-是。
     * @return 保存后的JSON信息。
     */
    JSONObject modify(EditorModel editor, int template);

    /**
     * 修改名称。
     *
     * @param id   ID值。
     * @param name 名称。
     * @return 编辑器信息。
     */
    JSONObject name(String id, String name);

    /**
     * 生成预览图。
     *
     * @param id 编辑器ID。
     * @return 异步ID。
     */
    String image(String id);

    /**
     * 设置主截图。
     *
     * @param id  ID值。
     * @param uri 主截图。
     */
    void screenshot(String id, String uri);

    /**
     * 设置状态。
     *
     * @param id    ID值。
     * @param state 状态。
     * @return 编辑器信息。
     */
    JSONObject state(String id, int state);

    /**
     * 异步导出PDF。
     *
     * @param id 编辑器ID。
     * @return 异步ID。
     */
    String pdf(String id);

    /**
     * 复制。
     *
     * @param id   源ID值。
     * @param type 目标类型。
     * @return 新编辑器信息。
     */
    JSONObject copy(String id, String type);

    /**
     * 修改编辑器修改时间。
     *
     * @param map 数据集，id-modify对。
     */
    void modify(Map<String, Long> map);

    /**
     * 修改编辑器修改时间。
     *
     * @param id ID值。
     */
    void modify(String id);

    /**
     * 删除。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 从回收站还原。
     *
     * @param id ID值。
     */
    void restore(String id);

    /**
     * 搜索模板信息集。
     *
     * @param type     类型。
     * @param template 模板。
     * @param labels   标签集。
     * @param words    关键词集。
     * @param order    排序规则。
     * @return 编辑器信息集。
     */
    JSONObject searchTemplate(String type, int template, String[] labels, String[] words, Order order);

    /**
     * 重建搜索索引。
     *
     * @param type     类型。
     * @param template 模板。
     * @return 异步ID。
     */
    String resetSearchIndex(String type, int template);
}
