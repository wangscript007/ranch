package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AccountModel.NAME + ".service")
public class AccountServiceImpl implements AccountService {
    private static final String LOCK_USER = AccountModel.NAME + ".service.lock:";

    @Inject
    private ModelHelper modelHelper;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccountDao accountDao;

    @Override
    public JSONObject deposit(String owner, int type, int amount) {
        AccountModel account = find(owner, type);
        if (account == null)
            return null;

        account.setBalance(account.getBalance() + amount);
        account.setDeposit(account.getDeposit() + amount);

        return save(account);
    }

    @Override
    public JSONObject withdraw(String owner, int type, int amount) {
        AccountModel account = find(owner, type);
        if (account == null)
            return null;

        account.setBalance(account.getBalance() - amount);
        account.setWithdraw(account.getWithdraw() + amount);

        return save(account);
    }

    private AccountModel find(String owner, int type) {
        String user = userHelper.id();
        String lockId = lockHelper.lock(LOCK_USER + user, 1000L);
        if (lockId == null)
            return null;

        AccountModel account = accountDao.find(user, owner, type);
        if (account == null) {
            account = new AccountModel();
            account.setUser(user);
            account.setOwner(owner);
            account.setType(type);
        }
        account.setLockId(lockId);

        return account;
    }

    private JSONObject save(AccountModel account) {
        accountDao.save(account);
        lockHelper.unlock(account.getLockId());

        return modelHelper.toJson(account);
    }
}
