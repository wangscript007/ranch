package org.lpw.ranch.editor.graphic;

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
@Repository(GraphicModel.NAME + ".dao")
class GraphicDaoImpl implements GraphicDao {
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<GraphicModel> query(String type, String name, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_name", DaoOperation.Equals, name);

        return liteOrm.query(new LiteQuery(GraphicModel.class).where(where.toString()).order("c_sort").size(pageSize).page(pageNum),
                args.toArray());
    }

    @Override
    public GraphicModel findById(String id) {
        return liteOrm.findById(GraphicModel.class, id);
    }

    @Override
    public void save(GraphicModel graphic) {
        liteOrm.save(graphic);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(GraphicModel.class, id);
    }
}
