package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.ranch.recycle.Recycle;
import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface DocDao {
    DocModel findById(String id);

    PageList<DocModel> query(String key, String owner, String author, String subject, Audit audit, Recycle recycle, int pageSize, int pageNum);

    PageList<DocModel> queryByAuthor(String author, int pageSize, int pageNum);

    void save(DocModel doc);

    void read(String id, int n);

    void favorite(String id, int n);

    void comment(String id, int n);
}
