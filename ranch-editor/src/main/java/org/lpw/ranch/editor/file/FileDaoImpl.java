package org.lpw.ranch.editor.file;

import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
        return liteOrm.query(new LiteQuery(FileModel.class).where("c_editor=?"), new Object[]{editor});
    }

    @Override
    public FileModel findById(String id) {
        return liteOrm.findById(FileModel.class, id);
    }

    @Override
    public FileModel find(String editor, String type) {
        return liteOrm.findOne(new LiteQuery(FileModel.class).where("c_editor=? and c_type=?"), new Object[]{editor, type});
    }

    @Override
    public Map<String, Integer> count() {
        Map<String, Integer> map = new HashMap<>();
        sql.query("SELECT c_editor,SUM(c_download) FROM " + modelTables.get(FileModel.class).getTableName() + " GROUP BY c_editor",
                null).forEach(list -> map.put((String) list.get(0), numeric.toInt(list.get(1))));

        return map;
    }

    @Override
    public Set<String> editors() {
        Set<String> set = new HashSet<>();
        sql.query("SELECT c_editor,COUNT(*) FROM " + modelTables.get(FileModel.class).getTableName() + " GROUP BY c_editor",
                null).forEach(list -> {
            if (numeric.toInt(list.get(1)) == 4)
                set.add((String) list.get(0));
        });

        return set;
    }

    @Override
    public void save(FileModel file) {
        liteOrm.save(file);
    }
}
