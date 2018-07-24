package org.lpw.ranch.doc.topic;

import com.alibaba.fastjson.JSONArray;
import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.doc.DocModel;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

/**
 * @author lpw
 */
public interface TopicService {
    /**
     * 检索。
     *
     * @param classify 分类。
     * @param author   作者。
     * @param subject  标题，模糊匹配。
     * @param label    标签，模糊匹配。
     * @param type     类型。
     * @param audit    审核状态。
     * @return 主题集。
     */
    PageList<TopicModel> query(String classify, String author, String subject, String label, String type, Audit audit);

    /**
     * 查找分类ID集。
     *
     * @param doc 文档ID。
     * @return 分类ID集。
     */
    JSONArray classifies(String doc);

    /**
     * 保存。
     *
     * @param doc        文档ID。
     * @param classifies 分类ID集。
     */
    void save(DocModel doc, Set<String> classifies);

    /**
     * 设置审核状态。
     *
     * @param doc   文档ID。
     * @param audit 审核状态。
     */
    void audit(String doc, Audit audit);

    /**
     * 设置回收站状态。
     *
     * @param doc     文档ID。
     * @param recycle 回收站状态。
     */
    void recycle(String doc, Recycle recycle);
}
