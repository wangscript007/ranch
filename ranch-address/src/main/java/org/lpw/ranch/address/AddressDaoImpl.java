package org.lpw.ranch.address;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AddressModel.NAME + ".dao")
class AddressDaoImpl implements AddressDao {
    @Inject
    private LiteOrm liteOrm;
}
