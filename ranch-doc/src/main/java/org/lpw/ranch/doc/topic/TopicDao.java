package org.lpw.ranch.doc.topic;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface TopicDao {
    PageList<TopicModel> query(String classify, String author, String subject, String label, String type,
                               Audit audit, Recycle recycle, int pageSize, int pageNum);

    PageList<TopicModel> query(String doc);

    void save(TopicModel topic);

    void audit(String doc, Audit audit);

    void recycle(String doc, Recycle recycle);

    void delete(String doc);
}
