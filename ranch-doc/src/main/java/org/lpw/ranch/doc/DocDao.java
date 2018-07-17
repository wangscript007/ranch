package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface DocDao {
    DocModel findById(String id);

    PageList<DocModel> query(String key, String author, String subject, String label,
                             Audit audit, Recycle recycle, int pageSize, int pageNum);

    void save(DocModel doc);
}
