package org.lpw.ranch.editor;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(EditorModel.NAME + ".dao")
class EditorDaoImpl implements EditorDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public EditorModel findById(String id) {
        return liteOrm.findById(EditorModel.class, id);
    }

    @Override
    public void save(EditorModel editor) {
        liteOrm.save(editor);
    }
}
