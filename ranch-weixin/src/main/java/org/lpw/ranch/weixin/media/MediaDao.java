package org.lpw.ranch.weixin.media;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface MediaDao {
    PageList<MediaModel> query(String key, String appId, String type, String name, Timestamp[] time, int pageSize, int pageNum);

    MediaModel findById(String id);

    void save(MediaModel media);

    void delete(String id);
}
