package org.lpw.ranch.account;

/**
 * @author lpw
 */
interface AccountDao {
    AccountModel find(String user, String owner, int type);

    void save(AccountModel account);
}
