package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author lpw
 */
public interface PushService {
    /**
     * 推送器是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS_SENDER = PushModel.NAME + ".validator.sender.exists";
    /**
     * 引用键是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS_Key = PushModel.NAME + ".validator.key.exists";
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
     * @param id      ID值，不存在则新建。
     * @param key     引用键。
     * @param sender  推送器。
     * @param subject 标题。
     * @param content 内容。
     * @param state   状态：0-待审核；1-使用中。
     * @return 推送配置。
     */
    JSONObject save(String id, String key, String sender, String subject, String content, int state);

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
     * @param receiver 接收者。
     * @param map      参数集。
     * @return 推送结果：true-成功；false-失败。
     */
    boolean send(String key, String receiver, Map<String, String> map);
}
