package org.lpw.ranch.editor.screenshot;

import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Repository(ScreenshotModel.NAME + ".dao")
class ScreenshotDaoImpl implements ScreenshotDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private Sql sql;

    @Override
    public PageList<ScreenshotModel> query(String editor) {
        return liteOrm.query(new LiteQuery(ScreenshotModel.class).where("c_editor=?").order("c_index"), new Object[]{editor});
    }

    @Override
    public ScreenshotModel findById(String id) {
        return liteOrm.findById(ScreenshotModel.class, id);
    }

    @Override
    public ScreenshotModel find(String editor, String page) {
        return liteOrm.findOne(new LiteQuery(ScreenshotModel.class).where("c_editor=? and c_page=?"), new Object[]{editor, page});
    }

    @Override
    public void save(ScreenshotModel screenshot) {
        liteOrm.save(screenshot);
    }

    @Override
    public void delete(String editor) {
        liteOrm.delete(new LiteQuery(ScreenshotModel.class).where("c_editor=?"), new Object[]{editor});
    }

    @Override
    public Map<String,String> index(int size) {
        Map<String, String> map = new HashMap<>();
        sql.query("select c_editor,c_page from t_editor_screenshot where c_index=0 and c_page<>'' limit ?", new Object[]{size})
                .forEach(list -> map.put((String) list.get(1),(String) list.get(0)));

        return map;
    }

    @Override
    public void index(String page, int index) {
        sql.update("update t_editor_screenshot set c_index=? where c_page=?", new Object[]{index, page});
    }
}
