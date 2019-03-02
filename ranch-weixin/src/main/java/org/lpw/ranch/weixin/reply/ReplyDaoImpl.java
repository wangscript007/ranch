package org.lpw.ranch.weixin.reply;

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
@Repository(ReplyModel.NAME + ".dao")
class ReplyDaoImpl implements ReplyDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<ReplyModel> query(String key, String receiveType, String receiveMessage, int state, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_key", DaoOperation.Equals, key);
        daoHelper.where(where, args, "c_receive_type", DaoOperation.Equals, receiveType);
        daoHelper.where(where, args, "c_receive_message", DaoOperation.Equals, receiveMessage);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);

        return liteOrm.query(new LiteQuery(ReplyModel.class).where(where.toString()).order("c_key,c_receive_type,c_receive_message,c_sort")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public ReplyModel findById(String id) {
        return liteOrm.findById(ReplyModel.class, id);
    }

    @Override
    public void save(ReplyModel reply) {
        liteOrm.save(reply);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(ReplyModel.class, id);
    }
}
