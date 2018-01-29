package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface PushService {
    /**
     * 模板类型。
     */
    enum Type {
        /**
         * 标题。
         */
        Subject,
        /**
         * 内容。
         */
        Content
    }

    /**
     * 推送器是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS_SENDER = PushModel.NAME + ".validator.sender.exists";
    /**
     * 引用键是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS_KEY = PushModel.NAME + ".validator.key.exists";
    /**
     * 配置信息是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = PushModel.NAME + ".validator.exists";

    /**
     * 检索推送配置集。
     *
     * @param key     引用键，支持模糊匹配。
     * @param subject 标题，支持模糊匹配。
     * @param state   状态，-1表示全部。
     * @return 推送配置集。
     */
    JSONObject query(String key, String subject, int state);

    /**
     * 查找推送配置。
     *
     * @param id ID值。
     * @return 推送配置，不存在则返回null。
     */
    PushModel findById(String id);

    /**
     * 查找推送配置。
     *
     * @param key 引用键。
     * @return 推送配置，不存在则返回null。
     */
    PushModel findByKey(String key);

    /**
     * 保存推送配置。
     *
     * @param id       ID值，不存在则新建。
     * @param key      引用键。
     * @param sender   推送器。
     * @param appCode  APP编码。
     * @param subject  标题。
     * @param content  内容。
     * @param template 模板。
     * @param name     发送者名称。
     * @param state    状态：0-待审核；1-使用中。
     * @return 推送配置。
     */
    JSONObject save(String id, String key, String sender, String appCode, String subject, String content,
                    String template, String name, int state);

    /**
     * 设置状态。
     *
     * @param id    ID值。
     * @param state 状态：0-待审核；1-使用中。
     * @return 推送配置。
     */
    JSONObject state(String id, int state);

    /**
     * 删除推送配置。
     *
     * @param id ID值。
     */
    void delete(String id);

    /**
     * 是否存在推送器。
     *
     * @param sender 推送器名称。
     * @return 存在则返回true；否则返回false。
     */
    boolean existsSender(String sender);

    /**
     * 推送。
     *
     * @param key      引用键。
     * @param user     用户。
     * @param receiver 接收者。
     * @param args     参数集。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(String key, String user, String receiver, JSONObject args);

    /**
     * 解析模板数据。
     *
     * @param type     模板类型。
     * @param key      引用键。
     * @param template 模板。
     * @param args     参数集。
     * @return 解析后的数据。
     */
    String parse(Type type, String key, String template, JSONObject args);
}
