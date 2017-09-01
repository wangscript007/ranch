package org.lpw.ranch.account;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AccountDao {
//    PageList<AccountModel> query(String user,String owner,int type);

    PageList<AccountModel> query(String user);

    PageList<AccountModel> query(String user, String owner);

    AccountModel findById(String id);

    AccountModel find(String user, String owner, int type);

    void save(AccountModel account);
}
