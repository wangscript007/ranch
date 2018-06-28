package org.lpw.ranch.resource;

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
@Repository(ResourceModel.NAME + ".dao")
class ResourceDaoImpl implements ResourceDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<ResourceModel> query(String type, String name, String label, int state, String user, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_name", DaoOperation.Equals, name);
        daoHelper.where(where, args, "c_state", DaoOperation.Equals, state);
        daoHelper.where(where, args, "c_user", DaoOperation.Equals, user);
        daoHelper.like(null, where, args, "c_label", label);

        return liteOrm.query(new LiteQuery(ResourceModel.class).where(where.toString()).order("c_sort,c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public ResourceModel findById(String id) {
        return liteOrm.findById(ResourceModel.class, id);
    }

    @Override
    public void save(ResourceModel resource) {
        liteOrm.save(resource);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(ResourceModel.class, id);
    }
}
