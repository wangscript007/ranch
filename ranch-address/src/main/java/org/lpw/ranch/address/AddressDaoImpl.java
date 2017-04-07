package org.lpw.ranch.address;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AddressModel.NAME + ".dao")
class AddressDaoImpl implements AddressDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<AddressModel> query(String user) {
        return liteOrm.query(new LiteQuery(AddressModel.class).where("c_user=?").order("c_major desc,c_time desc"), new Object[]{user});
    }

    @Override
    public AddressModel findById(String id) {
        return liteOrm.findById(AddressModel.class, id);
    }

    @Override
    public void save(AddressModel address) {
        liteOrm.save(address);
    }

    @Override
    public void major(String user, int major) {
        liteOrm.update(new LiteQuery(AddressModel.class).set("c_major=?").where("c_user=?"), new Object[]{major, user});
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(AddressModel.class, id);
    }
}
