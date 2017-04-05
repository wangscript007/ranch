package org.lpw.ranch.address;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AddressDao {
    PageList<AddressModel> query(String user);

    AddressModel findById(String id);

    void save(AddressModel address);

    void major(String user, int major);
}
