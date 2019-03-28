package org.lpw.ranch.editor.file;

import org.lpw.ranch.editor.EditorModel;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Repository(FileModel.NAME + ".dao")
class FileDaoImpl implements FileDao {
    @Inject
    private Numeric numeric;
    @Inject
    private Sql sql;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private ModelTables modelTables;

    @Override
    public PageList<FileModel> query(String editor) {
        return liteOrm.query(new LiteQuery(EditorModel.class).where("c_editor=?"), new Object[]{editor});
    }

    @Override
    public FileModel find(String editor, String type) {
        return liteOrm.findOne(new LiteQuery(EditorModel.class).where("c_editor=? and c_type=?"), new Object[]{editor, type});
    }

    @Override
    public Map<String, Integer> count() {
        Map<String, Integer> map = new HashMap<>();
        sql.query("SELECT c_editor,SUM(c_download) FROM " + modelTables.get(FileModel.class).getTableName() + " GROUP BY c_editor",
                null).forEach(list -> map.put((String) list.get(0), numeric.toInt(list.get(1))));

        return map;
    }

    @Override
    public void save(FileModel file) {
        liteOrm.save(file);
    }
}
