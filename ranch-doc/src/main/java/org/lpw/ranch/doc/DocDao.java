package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.dao.orm.PageList;

import java.util.Set;

/**
 * @author lpw
 */
interface DocDao {
    PageList<DocModel> query(String author, String category, String subject, String label, String type,
                             Audit audit, Recycle recycle, int pageSize, int pageNum);

    PageList<DocModel> query(Set<String> ids, int pageSize, int pageNum);

    DocModel findById(String id);

    void save(DocModel doc);
}
