package org.lpw.ranch.dbtool.table;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(TableModel.NAME + ".dao")
class TableDaoImpl implements TableDao {
    @Inject
    private LiteOrm liteOrm;
}
