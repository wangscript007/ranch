package org.lpw.ranch.doc;

import com.alibaba.fastjson.JSONObject;
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

    JSONObject query();

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
     * @param doc 文档信息。
     */
    JSONObject create(DocModel doc);

    /**
     * 阅读。
     *
     * @param id ID值。
     * @return 内容。
     */
    String read(String id);

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
     * 刷新缓存。
     */
    void refresh();
}
