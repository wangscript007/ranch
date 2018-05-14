package org.lpw.ranch.editor.media;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface MediaDao {
    PageList<MediaModel> query(String user, String editor, int type, int pageSize, int pageNum);

    MediaModel findById(String id);

    void save(MediaModel media);

    void delete(String id);
}
