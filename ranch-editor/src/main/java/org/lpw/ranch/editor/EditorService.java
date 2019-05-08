package org.lpw.ranch.editor;

import com.alibaba.fastjson.JSONObject;

import java.sql.Timestamp;
import java.util.Map;

/**
 * @author lpw
 */
public interface EditorService {
    /**
     * Template最大值。
     */
    int MAX_TEMPLATE = 4;
    /**
     * 编辑器信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = EditorModel.NAME + ".validator.exists";
    /**
     * 编辑器状态是否可编辑验证器Bean名称。
     */
    String VALIDATOR_EDITABLE = EditorModel.NAME + ".validator.editable";
    /**
     * 可用验证器Bean名称。
     */
    String VALIDATOR_USABLE = EditorModel.NAME + ".validator.usable";

    /**
     * 检索编辑器信息集。
     *
     * @param user         用户ID。
     * @param uid          UID。
     * @param mobile       用户手机号。
     * @param email        用户Email。
     * @param nick         用户昵称。
     * @param template     模板。
     * @param type         类型。
     * @param name         名称。
     * @param label        标签。
     * @param group        分组。
     * @param price        价格。
     * @param vipPrice     VIP价格。
     * @param limitedPrice 限时价格。
     * @param modified     最小已修改根节点数。
     * @param states       状态集。
     * @param createStart  创建开始日期，格式：yyyy-MM-dd。
     * @param createEnd    创建结束日期，格式：yyyy-MM-dd。
     * @param modifyStart  编辑开始日期，格式：yyyy-MM-dd。
     * @param modifyEnd    编辑结束日期，格式：yyyy-MM-dd。
     * @param order        排序规则。
     * @return 编辑器信息集。
     */
    JSONObject query(String user, String uid, String mobile, String email, String nick, int template, String type, String name, String label,
                     String group, int price, int vipPrice, int limitedPrice, int modified, String[] states, String createStart, String createEnd,
                     String modifyStart, String modifyEnd, Order order);

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
     * 查找模板ID。
     *
     * @param id ID值。
     * @return 模板ID，不存在则返回null。
     */
    String findTemplate(String id);

    /**
     * 查找模板信息集。
     *
     * @param ids ID集。
     * @return 模板信息集。
     */
    JSONObject templates(String[] ids);

    /**
     * 分组不存在。
     *
     * @param group 分组。
     * @return 不存在则返回true；否则返回false。
     */
    boolean notExistsGroup(String group);

    /**
     * 可用。
     *
     * @param id ID值。
     * @return 可用返回true；否则返回false。
     */
    boolean usable(String id);

    /**
     * 无标记（水印）。
     *
     * @param id ID值。
     * @return 无标记返回true；否则返回false。
     */
    boolean nomark(String id);

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
     * 设置预览图。
     *
     * @param id    编辑器ID。
     * @param image 预览图。
     */
    void image(String id, String image);

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
     * @param id    编辑器ID。
     * @param email 发送Email，不为空将在导出完成后发送Email。
     * @return 异步ID。
     */
    String pdf(String id, String email);

    /**
     * 异步导出图片集。
     *
     * @param id    编辑器ID。
     * @param email 发送Email，不为空将在导出完成后发送Email。
     * @return 异步ID。
     */
    String images(String id, String email);

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
     * 修改标签。
     *
     * @param map 标签集。
     */
    void labels(Map<String, StringBuilder> map);

    /**
     * 设置价格。
     *
     * @param ids          ID集。
     * @param type         类型。
     * @param group        分组。
     * @param price        价格，单位：分。
     * @param vipPrice     VIP价格，单位：分。
     * @param limitedPrice 限时价格，单位：分。
     * @param limitedTime  限时时间。
     */
    void price(String[] ids, String type, String group, int price, int vipPrice, int limitedPrice, Timestamp limitedTime);

    /**
     * 修改分组。
     *
     * @param type     类型。
     * @param oldGroup 旧分组。
     * @param newGroup 新分组。
     */
    void group(String type, String oldGroup, String newGroup);

    /**
     * 修改顺序。
     *
     * @param type  类型。
     * @param ids   ID集。
     * @param sorts 顺序集。
     */
    void sort(String type, String[] ids, String[] sorts);

    /**
     * 更新下载次数。
     *
     * @param map 编辑器-下载次数集。
     */
    void download(Map<String, Integer> map);

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
     * 发布。
     *
     * @param id     ID值。
     * @param width  主页面宽度。
     * @param height 主页面高度。
     * @return 异步ID。
     */
    String publish(String id, int width, int height);

    /**
     * 发布。
     *
     * @param type   类型。
     * @param types  发布类型集，为空表示全部。
     * @param width  主页面宽度。
     * @param height 主页面高度。
     * @return 异步ID。
     */
    String publishes(String type, String[] types, int width, int height);

    /**
     * 搜索模板信息集。
     *
     * @param type     类型。
     * @param template 模板。
     * @param labels   标签集。
     * @param words    关键词集。
     * @param free     是否免费。
     * @param nofree   是否收费。
     * @param order    排序规则。
     * @return 编辑器信息集。
     */
    JSONObject searchTemplate(String type, int template, String[] labels, String[] words, boolean free, boolean nofree, Order order);

    /**
     * 重建搜索索引。
     *
     * @param type     类型。
     * @param template 模板。
     * @return 异步ID。
     */
    String resetSearchIndex(String type, int template);

    /**
     * 搜索模板信息集。
     *
     * @param type     类型。
     * @param template 模板。
     * @param label    标签。
     * @param size     数量。
     * @return 编辑器信息集。
     */
    JSONObject searchTemplate(String type, int template, String label, int size);

    /**
     * 获取截图渲染URL。
     *
     * @param sid Session ID。
     * @param id  ID值。
     * @return 截图渲染URL，未配置则返回null。
     */
    String getCapture(String sid, String id);

    /**
     * 获取截图渲染URL（无水印）。
     *
     * @param sid Session ID。
     * @param id  ID值。
     * @return 截图渲染URL，未配置则返回null。
     */
    String getCaptureNomark(String sid, String id);

    /**
     * 获取截图渲染URL（有水印）。
     *
     * @param sid Session ID。
     * @param id  ID值。
     * @return 截图渲染URL，未配置则返回null。
     */
    String getCaptureMark(String sid, String id);

    /**
     * 获取截图渲染等待时长。
     *
     * @return 截图渲染等待时长，单位：秒。
     */
    int getCaptureWait();
}
