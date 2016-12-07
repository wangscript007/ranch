package org.lpw.ranch.doc;

import org.lpw.ranch.audit.Audit;

/**
 * @author lpw
 */
interface DocDao {
    DocModel findById(String id);

    void save(DocModel doc);

    void read(String id, int n);

    void favorite(String id, int n);

    void comment(String id, int n);

    void audit(String[] ids, Audit audit);
}
