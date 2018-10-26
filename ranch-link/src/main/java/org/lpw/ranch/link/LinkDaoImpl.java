package org.lpw.ranch.link;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Repository(LinkModel.NAME + ".dao")
class LinkDaoImpl implements LinkDao {
    @Inject
    private Numeric numeric;
    @Inject
    private Sql sql;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private DaoHelper daoHelper;

    @Override
    public PageList<LinkModel> query1(String type, String id1, int pageSize, int pageNum) {
        return query(type, 1, id1, pageSize, pageNum);
    }

    @Override
    public PageList<LinkModel> query2(String type, String id2, int pageSize, int pageNum) {
        return query(type, 2, id2, pageSize, pageNum);
    }

    private PageList<LinkModel> query(String type, int i, String value, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(LinkModel.class).where("c_type=? and c_id" + i + "=?").order("c_time desc").size(pageSize).page(pageNum), new Object[]{type, value});
    }

    @Override
    public int count1(String type, String id1) {
        return count(type, 1, id1);
    }

    @Override
    public int count2(String type, String id2) {
        return count(type, 2, id2);
    }

    private int count(String type, int i, String value) {
        return numeric.toInt(sql.query("select count(*) from t_link where c_type=? and c_id" + i + "=?",
                new Object[]{type, value}).get(0, 0));
    }

    @Override
    public LinkModel find(String type, String id1, String id2) {
        return liteOrm.findOne(new LiteQuery(LinkModel.class).where("c_type=? and c_id1=? and c_id2=?"), new Object[]{type, id1, id2});
    }

    @Override
    public void save(LinkModel link) {
        liteOrm.save(link);
    }

    @Override
    public void delete(String type, String id1, String id2) {
        StringBuilder where = new StringBuilder("c_type=?");
        List<Object> args = new ArrayList<>();
        args.add(type);
        daoHelper.where(where, args, "c_id1", DaoOperation.Equals, id1);
        daoHelper.where(where, args, "c_id2", DaoOperation.Equals, id2);

        liteOrm.delete(new LiteQuery(LinkModel.class).where(where.toString()), args.toArray());
    }
}
