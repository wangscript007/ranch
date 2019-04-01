package org.lpw.ranch.editor.buy;

import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.model.ModelTables;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Repository(BuyModel.NAME + ".dao")
class BuyDaoImpl implements BuyDao {
    @Inject
    private Numeric numeric;
    @Inject
    private ModelTables modelTables;
    @Inject
    private Sql sql;
    @Inject
    private LiteOrm liteOrm;

    @Override
    public BuyModel find(String user, String editor) {
        return liteOrm.findOne(new LiteQuery(BuyModel.class).where("c_editor=? and c_user=?"), new Object[]{editor, user});
    }

    @Override
    public Map<String, Integer> count() {
        Map<String, Integer> map = new HashMap<>();
        sql.query("SELECT c_editor,COUNT(*) FROM " + modelTables.get(BuyModel.class).getTableName() + " GROUP BY c_editor", null)
                .forEach(list -> map.put((String) list.get(0), numeric.toInt(list.get(1))));

        return map;
    }

    @Override
    public void save(BuyModel buy) {
        liteOrm.save(buy);
    }
}
