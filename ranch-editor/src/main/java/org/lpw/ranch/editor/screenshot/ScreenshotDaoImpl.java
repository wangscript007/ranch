package org.lpw.ranch.editor.screenshot;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(ScreenshotModel.NAME + ".dao")
class ScreenshotDaoImpl implements ScreenshotDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<ScreenshotModel> query(String editor) {
        return liteOrm.query(new LiteQuery(ScreenshotModel.class).where("c_editor=?"), new Object[]{editor});
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
}
