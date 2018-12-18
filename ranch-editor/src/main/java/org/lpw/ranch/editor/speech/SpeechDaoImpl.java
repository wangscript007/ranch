package org.lpw.ranch.editor.speech;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
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
@Repository(SpeechModel.NAME + ".dao")
class SpeechDaoImpl implements SpeechDao {
    @Inject
    private Sql sql;
    @Inject
    private ModelTables modelTables;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<SpeechModel> query(String user, int state, Timestamp[] times, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.where(where, args, "c_time", DaoOperation.GreaterEquals, times[0]);
        daoHelper.where(where, args, "c_time", DaoOperation.LessEquals, times[1]);

        return liteOrm.query(new LiteQuery(SpeechModel.class).where(where.toString())
                .order("c_time desc").size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<SpeechModel> query(int state) {
        return liteOrm.query(new LiteQuery(SpeechModel.class).where("c_state<?"), new Object[]{state});
    }

    @Override
    public SpeechModel findById(String id) {
        return liteOrm.findById(SpeechModel.class, id);
    }

    @Override
    public void save(SpeechModel speech) {
        liteOrm.save(speech);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(SpeechModel.class, id);
    }
}
