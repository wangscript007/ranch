package org.lpw.ranch.form;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(FormModel.NAME + ".dao")
class FormDaoImpl implements FormDao {
    @Inject
    private LiteOrm liteOrm;
}
