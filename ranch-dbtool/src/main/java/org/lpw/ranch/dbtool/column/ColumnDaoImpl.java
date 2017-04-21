package org.lpw.ranch.dbtool.column;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(ColumnModel.NAME + ".dao")
class ColumnDaoImpl implements ColumnDao {
    @Inject
    private LiteOrm liteOrm;
}
