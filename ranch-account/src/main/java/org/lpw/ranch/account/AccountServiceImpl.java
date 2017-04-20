package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.log.LogService;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AccountModel.NAME + ".service")
public class AccountServiceImpl implements AccountService {
    private static final String LOCK_USER = AccountModel.NAME + ".service.lock:";

    @Inject
    private Validator validator;
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
    public JSONArray query(String user, String owner) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        if (owner != null && owner.trim().length() == 0)
            owner = "";
        PageList<AccountModel> pl = owner == null ? accountDao.query(user) : accountDao.query(user, owner);

        return modelHelper.toJson(pl.getList());
    }

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
        if (account == null || account.getBalance() - amount < 0)
            return null;

        account.setWithdraw(account.getWithdraw() + amount);

        return save(account, -1 * amount, "withdraw", LogService.State.New);
    }

    @Override
    public JSONObject reward(String user, String owner, int type, int amount) {
        AccountModel account = find(user, owner, type);
        if (account == null)
            return null;

        account.setReward(account.getReward() + amount);

        return save(account, amount, "reward", LogService.State.Complete);
    }

    @Override
    public JSONObject profit(String user, String owner, int type, int amount) {
        AccountModel account = find(user, owner, type);
        if (account == null)
            return null;

        account.setProfit(account.getProfit() + amount);

        return save(account, amount, "profit", LogService.State.Complete);
    }

    @Override
    public JSONObject consume(String owner, int type, int amount) {
        AccountModel account = find(userHelper.id(), owner, type);
        if (account == null || account.getBalance() - amount < 0)
            return null;

        account.setConsume(account.getConsume() + amount);

        return save(account, -1 * amount, "consume", LogService.State.Complete);
    }

    private AccountModel find(String user, String owner, int type) {
        String lockId = lockHelper.lock(LOCK_USER + user, 1000L);
        if (lockId == null)
            return null;

        if (validator.isEmpty(owner))
            owner = "";
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
