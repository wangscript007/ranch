package org.lpw.ranch.dbtool.schema;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(SchemaModel.NAME + ".dao")
class SchemaDaoImpl implements SchemaDao {
    @Inject
    private LiteOrm liteOrm;
}
