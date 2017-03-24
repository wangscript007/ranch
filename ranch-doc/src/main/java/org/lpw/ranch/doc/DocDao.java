package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;
import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface DocDao {
    DocModel findById(String id);

    PageList<DocModel> queryByKey(Audit audit, String key, int pageSize, int pageNum);

    PageList<DocModel> queryByOwner(Audit audit, String owner, int pageSize, int pageNum);

    PageList<DocModel> queryByAuthor(Audit audit, String author, int pageSize, int pageNum);

    PageList<DocModel> queryByAuthor(String author, int pageSize, int pageNum);

    void save(DocModel doc);

    void read(String id, int n);

    void favorite(String id, int n);

    void comment(String id, int n);
}
