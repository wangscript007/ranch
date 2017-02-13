package org.lpw.ranch.doc;

/**
 * @author lpw
 */
interface DocDao {
    DocModel findById(String id);

    void save(DocModel doc);

    void read(String id, int n);

    void favorite(String id, int n);

    void comment(String id, int n);
}
