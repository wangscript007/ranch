package org.lpw.ranch.editor.screenshot;

import org.lpw.tephra.dao.orm.PageList;

import java.util.Map;

/**
 * @author lpw
 */
interface ScreenshotDao {
    PageList<ScreenshotModel> query(String editor);

    ScreenshotModel find(String editor, String page);

    void save(ScreenshotModel screenshot);

    void delete(String editor);

    Map<String, String> index(int size);

    void index(String page, int index);
}
