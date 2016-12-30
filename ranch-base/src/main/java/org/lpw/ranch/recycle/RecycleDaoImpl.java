package org.lpw.ranch.recycle;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository("ranch.recycle.dao")
public class RecycleDaoImpl implements RecycleDao {
    @Inject
    private Validator validator;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public <T extends RecycleModel> void recycle(Class<T> modelClass, String id, Recycle recycle) {
        if (modelClass == null || validator.isEmpty(id) || recycle == null)
            return;

        liteOrm.update(new LiteQuery(modelClass).set(recycle.getSql()).where("c_id=?"), new Object[]{id});
    }
}
