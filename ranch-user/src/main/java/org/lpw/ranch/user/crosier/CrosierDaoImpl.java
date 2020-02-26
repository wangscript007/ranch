package org.lpw.ranch.user.crosier;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(CrosierModel.NAME + ".dao")
class CrosierDaoImpl implements CrosierDao {
    @Inject
    private LiteOrm liteOrm;
}
