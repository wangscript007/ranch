package org.lpw.ranch.editor.media;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
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
    public PageList<MediaModel> query(String user, String editor, int type, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_editor", DaoOperation.Equals, editor);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);

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
    public void deleteById(String id) {
        liteOrm.deleteById(MediaModel.class, id);
    }

    @Override
    public void deleteByEditor(String editor) {
        liteOrm.delete(new LiteQuery(MediaModel.class).where("c_editor=?"), new Object[]{editor});
    }
}
