package org.lpw.ranch.editor.element;

import org.lpw.tephra.dao.auto.Executer;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.jdbc.SqlTable;
import org.lpw.tephra.dao.model.ModelTable;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Repository(ElementModel.NAME + ".dao")
class ElementDaoImpl implements ElementDao {
    @Inject
    private Numeric numeric;
    @Inject
    private ModelTables modelTables;
    @Inject
    private Sql sql;
    @Inject
    private LiteOrm liteOrm;
    @Inject
    private Executer executer;
    private ModelTable modelTable;
    private String tableName;

    @Override
    public PageList<ElementModel> query(String editor, String parent) {
        create(editor);

        return liteOrm.query(new LiteQuery(ElementModel.class).where("c_editor=? and c_parent=?").order("c_sort"),
                new Object[]{editor, parent});
    }

    @Override
    public ElementModel findById(String id, String editor) {
        create(editor);

        return liteOrm.findById(ElementModel.class, id);
    }

    @Override
    public Map<String, Long> modify(long modify) {
        Map<String, Long> map = new HashMap<>();
        String prefix = getTableName() + "_";
        executer.tables("").forEach(table -> {
            if (!table.startsWith(prefix))
                return;

            SqlTable sqlTable = sql.query("SELECT c_editor,MAX(c_modify) FROM " + table
                    + " WHERE c_modify>=? GROUP BY c_editor", new Object[]{modify});
            for (int i = 0; i < sqlTable.getRowCount(); i++)
                map.put(sqlTable.get(i, 0), numeric.toLong(sqlTable.get(i, 1)));
        });

        return map;
    }

    @Override
    public void save(ElementModel element) {
        create(element.getEditor());
        liteOrm.save(element);
    }

    @Override
    public void delete(ElementModel element) {
        create(element.getEditor());
        liteOrm.delete(element);
    }

    private void create(String editor) {
        executer.create(ElementModel.class, getTableName(editor));
    }

    private String getTableName(String editor) {
        if (modelTable == null)
            modelTable = modelTables.get(ElementModel.class);
        String iTableName = getTableName() + "_" + editor.substring(0, 2);
        modelTable.setInstantTableName(iTableName);

        return iTableName;
    }

    private String getTableName() {
        if (tableName == null)
            tableName = ElementModel.class.getAnnotation(Table.class).name();

        return tableName;
    }
}
