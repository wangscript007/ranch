package org.lpw.ranch.editor;

import org.lpw.ranch.util.DaoHelper;
import org.lpw.ranch.util.DaoOperation;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
@Repository(EditorModel.NAME + ".dao")
class EditorDaoImpl implements EditorDao {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
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
    public PageList<EditorModel> query(Set<String> ids, int template, String type, String name, String label, String group, int price, int vipPrice,
                                       int limitedPrice, Timestamp limitedTime, int modified, Set<Integer> states, Timestamp createStart,
                                       Timestamp createEnd, Timestamp modifyStart, Timestamp modifyEnd, Order order, int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.in(where, args, "c_id", ids);
        daoHelper.where(where, args, "c_template", DaoOperation.Equals, template);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_modified", DaoOperation.GreaterEquals, modified);
        daoHelper.in(where, args, "c_state", states);
        daoHelper.where(where, args, "c_create", DaoOperation.GreaterEquals, createStart);
        daoHelper.where(where, args, "c_create", DaoOperation.LessEquals, createEnd);
        daoHelper.where(where, args, "c_modify", DaoOperation.GreaterEquals, modifyStart);
        daoHelper.where(where, args, "c_modify", DaoOperation.LessEquals, modifyEnd);
        daoHelper.where(where, args, "c_group", DaoOperation.Equals, group);
        daoHelper.where(where, args, "c_price", DaoOperation.Equals, price);
        daoHelper.where(where, args, "c_vip_price", DaoOperation.Equals, vipPrice);
        daoHelper.where(where, args, "c_limited_price", DaoOperation.Equals, limitedPrice);
        daoHelper.where(where, args, "c_limited_time", DaoOperation.GreaterEquals, limitedTime);
        daoHelper.like(null, where, args, "c_name", name);
        daoHelper.like(null, where, args, "c_label", label);

        return liteOrm.query(new LiteQuery(EditorModel.class).where(where.toString()).order(order.by())
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public PageList<EditorModel> query(int template, String type, int state, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(EditorModel.class).where("c_template=? and c_type=? and c_state=?")
                .size(pageSize).page(pageNum), new Object[]{template, type, state});
    }

    @Override
    public PageList<EditorModel> query(Timestamp[] modify) {
        return liteOrm.query(new LiteQuery(EditorModel.class).where("c_modify between ? and ?"), new Object[]{modify[0], modify[1]});
    }

    @Override
    public PageList<EditorModel> search(Set<String> ids, int template, String type, boolean free, boolean vipFree, Order order,
                                        int pageSize, int pageNum) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.in(where, args, "c_id", ids);
        daoHelper.where(where, args, "c_template", DaoOperation.Equals, template);
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        where.append(" AND c_state=3");
        if (vipFree)
            where.append(" AND c_vip_price=0");
        if (free) {
            where.append(" AND (c_price=0 OR (c_limited_price=0 AND c_limited_time>=?))");
            args.add(dateTime.now());
        }

        return liteOrm.query(new LiteQuery(EditorModel.class).where(where.toString()).order(order.by())
                .size(pageSize).page(pageNum), args.toArray());
    }

    @Override
    public EditorModel findById(String id) {
        return liteOrm.findById(EditorModel.class, id);
    }

    @Override
    public EditorModel findByGroup(String group) {
        return liteOrm.findOne(new LiteQuery(EditorModel.class).where("c_group=? and c_template>0"), new Object[]{group});
    }

    @Override
    public PageList<EditorModel> templates(Set<String> ids) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        daoHelper.in(where, args, "c_id", ids);
        daoHelper.where(where, args, "c_template", DaoOperation.Greater, 0);

        return liteOrm.query(new LiteQuery(EditorModel.class).where(where.toString()), args.toArray());
    }

    @Override
    public List<String> templates(String type, int state) {
        List<String> list = new ArrayList<>();
        sql.query("SELECT c_id FROM " + modelTables.get(EditorModel.class).getTableName()
                        + " WHERE c_template IN(1,2) AND c_type=? AND c_state=?",
                new Object[]{type, state}).forEach(l -> list.add((String) l.get(0)));

        return list;
    }

    @Override
    public Map<String, Set<Integer>> typeTemplates() {
        Map<String, Set<Integer>> map = new HashMap<>();
        sql.query("SELECT c_type,c_template FROM " + modelTables.get(EditorModel.class).getTableName() + " WHERE c_state=3", null)
                .forEach(list -> map.computeIfAbsent((String) list.get(0), key -> new HashSet<>()).add(numeric.toInt(list.get(1))));

        return map;
    }

    @Override
    public void save(EditorModel editor) {
        liteOrm.save(editor);
        liteOrm.close();
    }

    @Override
    public void price(String[] ids, String type, String group, int price, int vipPrice, int limitedPrice, Timestamp limitedTime) {
        StringBuilder where = new StringBuilder();
        List<Object> args = new ArrayList<>();
        if (!validator.isEmpty(ids))
            daoHelper.in(where, args, "c_id", new HashSet<>(Arrays.asList(ids)));
        daoHelper.where(where, args, "c_type", DaoOperation.Equals, type);
        daoHelper.where(where, args, "c_template", DaoOperation.Greater, 0);
        daoHelper.where(where, args, "c_group", DaoOperation.Equals, group);
        Object[] objects = new Object[4 + args.size()];
        int i = 0;
        objects[i++] = price;
        objects[i++] = vipPrice;
        objects[i++] = limitedPrice;
        objects[i++] = limitedTime;
        for (Object object : args)
            objects[i++] = object;

        liteOrm.update(new LiteQuery(EditorModel.class).set("c_price=?,c_vip_price=?,c_limited_price=?,c_limited_time=?")
                .where(where.toString()), objects);
    }

    @Override
    public void group(String type, String oldGroup, String newGroup) {
        liteOrm.update(new LiteQuery(EditorModel.class).set("c_group=?").where("c_type=? and c_group=?"), new Object[]{newGroup, type, oldGroup});
    }

    @Override
    public void sort(String id, String type, int sort) {
        liteOrm.update(new LiteQuery(EditorModel.class).set("c_sort=?").where("c_id=? and c_template>0 and c_type=?"),
                new Object[]{sort, id, type});
    }

    @Override
    public void download(String id, int count) {
        liteOrm.update(new LiteQuery(EditorModel.class).set("c_download=?").where("c_id=?"), new Object[]{count, id});
        liteOrm.close();
    }

    @Override
    public void buy(String id, int count) {
        liteOrm.update(new LiteQuery(EditorModel.class).set("c_buy=?").where("c_id=?"), new Object[]{count, id});
        liteOrm.close();
    }
}
