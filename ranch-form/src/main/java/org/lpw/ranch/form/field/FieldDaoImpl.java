package org.lpw.ranch.form.field;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(FieldModel.NAME + ".dao")
class FieldDaoImpl implements FieldDao {
    @Inject
    private LiteOrm liteOrm;
}
