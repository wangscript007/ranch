package org.lpw.ranch.account;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AccountModel.NAME + ".dao")
class AccountDaoImpl implements AccountDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public AccountModel find(String user, String owner, int type) {
        return liteOrm.findOne(new LiteQuery(AccountModel.class).where("c_user=? and c_owner=? and c_type=?"), new Object[]{user, owner, type});
    }

    @Override
    public void save(AccountModel account) {
        liteOrm.save(account);
    }
}
