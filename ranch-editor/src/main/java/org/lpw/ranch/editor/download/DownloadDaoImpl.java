package org.lpw.ranch.editor.download;

import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lpw
 */
@Repository(DownloadModel.NAME + ".dao")
class DownloadDaoImpl implements DownloadDao {
    @Inject
    private Sql sql;
    @Inject
    private ModelTables modelTables;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public Set<String> editors(String user, Timestamp time) {
        Set<String> set = new HashSet<>();
        sql.query("SELECT c_editor FROM " + modelTables.get(DownloadModel.class).getTableName() + " WHERE c_time>=? AND c_user=?",
                new Object[]{time, user}).forEach(list -> set.add((String) list.get(0)));

        return set;
    }

    @Override
    public void save(DownloadModel download) {
        liteOrm.save(download);
    }

    @Override
    public void delete(Timestamp time) {
        liteOrm.delete(new LiteQuery(DownloadModel.class).where("c_time<?"), new Object[]{time});
    }
}
