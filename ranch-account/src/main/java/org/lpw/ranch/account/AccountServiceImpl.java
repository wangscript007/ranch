package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.log.LogService;
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
    private LogService logService;
    @Inject
    private AccountDao accountDao;

    @Override
    public JSONObject deposit(String user, String owner, int type, int amount) {
        AccountModel account = find(user, owner, type);
        if (account == null)
            return null;

        account.setDeposit(account.getDeposit() + amount);

        return save(account, amount, "deposit", LogService.State.Complete);
    }

    @Override
    public JSONObject withdraw(String owner, int type, int amount) {
        AccountModel account = find(userHelper.id(), owner, type);
        if (account == null)
            return null;

        account.setWithdraw(account.getWithdraw() + amount);

        return save(account, -1 * amount, "withdraw", LogService.State.New);
    }

    private AccountModel find(String user, String owner, int type) {
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

    private JSONObject save(AccountModel account, int amount, String type, LogService.State state) {
        account.setBalance(account.getBalance() + amount);
        accountDao.save(account);
        logService.create(account, type, amount, state);
        lockHelper.unlock(account.getLockId());

        return modelHelper.toJson(account);
    }
}
