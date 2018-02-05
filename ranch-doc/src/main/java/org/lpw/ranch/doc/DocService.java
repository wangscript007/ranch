package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.audit.AuditService;

/**
 * @author lpw
 */
public interface DocService extends AuditService {
    /**
     * ID是否存在验证器Bean名称。
     */
    String VALIDATOR_EXISTS = DocModel.NAME + ".validator.exists";

    /**
     * 根据ID查找文档实例。
     *
     * @param id ID值。
     * @return 文档实例；如果不存在则返回null。
     */
    DocModel findById(String id);

    /**
     * 检索文档信息集。
     *
     * @param key     类型key。
     * @param owner   所有者ID。
     * @param author  作者ID。
     * @param subject 标题，模糊匹配。
     * @param audit   审核状态。
     * @return 文档信息集。
     */
    JSONObject query(String key, String owner, String author, String subject, Audit audit);

    /**
     * 检索当前用户的文档信息集。
     *
     * @return 文档信息集。
     */
    JSONObject queryByAuthor();

    /**
     * 检索文档信息集。
     *
     * @param key 类型key。
     * @return 文档信息集。
     */
    JSONObject queryByKey(String key);

    /**
     * 获取指定ID的文档信息集。
     *
     * @param ids ID集。
     * @return 文档信息集。
     */
    JSONObject get(String[] ids);

    /**
     * 保存文档信息。
     *
     * @param doc      文档信息。
     * @param markdown 是否为Markdown文档。
     */
    JSONObject save(DocModel doc, boolean markdown);

    /**
     * 获取文档源内容。
     *
     * @param id ID值。
     * @return 源内容。
     */
    String source(String id);

    /**
     * 阅读。
     *
     * @param id ID值。
     * @return 内容。
     */
    String read(String id);

    /**
     * 阅读，并返回JSON格式的文档数据。
     *
     * @param id ID值。
     * @return 文档数据。
     */
    JSONObject readJson(String id);

    /**
     * 增减收藏数。
     *
     * @param id ID值。
     * @param n  收藏数：正数表示增加，负数表示减少。
     */
    void favorite(String id, int n);

    /**
     * 增减评论数。
     *
     * @param id ID值。
     * @param n  评论数：正数表示增加，负数表示减少。
     */
    void comment(String id, int n);

    /**
     * 点赞。
     *
     * @param id ID值。
     */
    void praise(String id);
}
