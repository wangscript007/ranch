package org.lpw.ranch.editor.element;

import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.jdbc.SqlTable;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Repository(ElementModel.NAME + ".dao")
class ElementDaoImpl implements ElementDao {
    @Inject
    private ModelTables modelTables;
    @Inject
    private Sql sql;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<ElementModel> query(String editor, String parent) {
        return liteOrm.query(new LiteQuery(ElementModel.class).where("c_editor=? and c_parent=?").order("c_sort"),
                new Object[]{editor, parent});
    }

    @Override
    public ElementModel findById(String id) {
        return liteOrm.findById(ElementModel.class, id);
    }

    @Override
    public Map<String, Timestamp> modify(Timestamp timestamp) {
        Map<String, Timestamp> map = new HashMap<>();
        SqlTable sqlTable = sql.query("SELECT c_editor,MAX(c_modify) FROM " + modelTables.get(ElementModel.class).getTableName()
                + " WHERE c_modify>=? GROUP BY c_editor", new Object[]{timestamp});
        for (int i = 0; i < sqlTable.getRowCount(); i++)
            map.put(sqlTable.get(i, 0), sqlTable.get(i, 1));

        return map;
    }

    @Override
    public void save(ElementModel element) {
        liteOrm.save(element);
    }

    @Override
    public void delete(ElementModel element) {
        liteOrm.delete(element);
    }
}
