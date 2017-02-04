package org.lpw.ranch.recycle;

import net.sf.json.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Component("ranch.recycle.helper")
public class RecycleHelperImpl implements RecycleHelper {
    @Inject
    private Validator validator;
    @Inject
    private Pagination pagination;
    @Inject
    private RecycleDao recycleDao;

    @Override
    public <T extends RecycleModel> void delete(Class<T> modelClass, String id) {
        recycle(modelClass, id, Recycle.Yes);
    }

    @Override
    public <T extends RecycleModel> void restore(Class<T> modelClass, String id) {
        recycle(modelClass, id, Recycle.No);
    }

    private <T extends RecycleModel> void recycle(Class<T> modelClass, String id, Recycle recycle) {
        if (modelClass == null || validator.isEmpty(id))
            return;

        recycleDao.recycle(modelClass, id, recycle);
    }

    @Override
    public <T extends RecycleModel> JSONObject recycle(Class<T> modelClass) {
        return recycleDao.recycle(modelClass, pagination.getPageSize(), pagination.getPageNum()).toJson();
    }
}
