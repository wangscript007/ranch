package org.lpw.ranch.weixin.media;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(MediaModel.NAME + ".dao")
class MediaDaoImpl implements MediaDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<MediaModel> query(String key, String appId, String type, String name, Timestamp[] time, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_key", DaoOperation.Equals, key);
        daoHelper.where(where, args, "c_app_id", DaoOperation.Equals, appId);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, time[0]);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, time[1]);
        daoHelper.like(null, where, args, "c_name", name);

        return liteOrm.query(new LiteQuery(MediaModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public MediaModel findById(String id) {
        return liteOrm.findById(MediaModel.class, id);
    }

    @Override
    public void save(MediaModel media) {
        liteOrm.save(media);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(MediaModel.class, id);
    }
}
