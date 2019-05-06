package org.lpw.ranch.link;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private ModelTables modelTables;
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
        return liteOrm.query(new LiteQuery(LinkModel.class).where("c_type=? and c_id" + i + "=?").order("c_time desc")
                .size(pageSize).page(pageNum), new Object[]{type, value});
    }

    @Override
    public PageList<LinkModel> query1(String type, Set<String> id1s, int pageSize, int pageNum) {
        return query(type, 1, id1s, pageSize, pageNum);
    }

    @Override
    public PageList<LinkModel> query2(String type, Set<String> id2s, int pageSize, int pageNum) {
        return query(type, 2, id2s, pageSize, pageNum);
    }

    private PageList<LinkModel> query(String type, int i, Set<String> values, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.in(where, args, "c_id" + i, values);

        return liteOrm.query(new LiteQuery(LinkModel.class).where(where.toString()).order("c_time desc")
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public int count(String type, String id1, String id2) {
        StringBuilder where = new StringBuilder("c_type=?");
        List<Object> args = new ArrayList<>();
        args.add(type);
        daoHelper.where(where, args, "c_id1", DaoOperation.Equals, id1);
        daoHelper.where(where, args, "c_id2", DaoOperation.Equals, id2);

        return liteOrm.count(new LiteQuery(LinkModel.class).where(where.toString()), args.toArray());
    }

    @Override
    public Map<String, Integer> count1(String type, Set<String> id1s) {
        return count(type, 1, id1s);
    }

    @Override
    public Map<String, Integer> count2(String type, Set<String> id1s) {
        return count(type, 2, id1s);
    }

    private Map<String, Integer> count(String type, int i, Set<String> values) {
        StringBuilder where = new StringBuilder("SELECT c_id").append(i).append(",COUNT(*) FROM ")
                .append(modelTables.get(LinkModel.class).getTableName()).append(" WHERE ");
        List<Object> args = new ArrayList<>();
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.in(where, args, "c_id" + i, values);
        Map<String, Integer> map = new HashMap<>();
        sql.query(where.append(" GROUP BY c_id").append(i).toString(), args.toArray())
                .forEach(list -> map.put((String) list.get(0), numeric.toInt(list.get(1))));

        return map;
    }

    @Override
    public LinkModel find(String type, String id1, String id2) {
        return liteOrm.findOne(new LiteQuery(LinkModel.class).where("c_type=? and c_id1=? and c_id2=?"), new Object[]{type, id1, id2});
    }

    @Override
    public void save(LinkModel link) {
        liteOrm.save(link);
        liteOrm.close();
    }

    @Override
    public void delete(String type, String id1, String id2) {
        StringBuilder where = new StringBuilder("c_type=?");
        List<Object> args = new ArrayList<>();
        args.add(type);
        daoHelper.where(where, args, "c_id1", DaoOperation.Equals, id1);
        daoHelper.where(where, args, "c_id2", DaoOperation.Equals, id2);

        liteOrm.delete(new LiteQuery(LinkModel.class).where(where.toString()), args.toArray());
        liteOrm.close();
    }
}
