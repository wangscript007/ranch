package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.lpw.ranch.account.type.AccountTypes;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(AccountModel.NAME + ".service")
public class AccountServiceImpl implements AccountService {
    private static final String LOCK_USER = AccountModel.NAME + ".service.lock:";
    private static final String CHECKSUM = AccountModel.NAME + ".service.checksum";

    @Inject
    private Validator validator;
    @Inject
    private Digest digest;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccountTypes accountTypes;
    @Inject
    private LogService logService;
    @Inject
    private AccountDao accountDao;
    @Value("${ranch.account.balance:0}")
    private int balance;

    @Override
    public JSONArray query(String user, String owner) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        PageList<AccountModel> pl = queryFromDao(user, owner);
        if (pl.getList().isEmpty()) {
            if (balance > 0)
                logService.pass(new String[]{deposit(user, owner, 0, balance, null).getString("logId")});
            else
                save(find(user, owner, 0));
            pl = queryFromDao(user, owner);
        }
        JSONArray array = modelHelper.toJson(pl.getList());

        return userHelper.fill(array, new String[]{"user"});
    }

    private PageList<AccountModel> queryFromDao(String user, String owner) {
        return owner == null ? accountDao.query(user) : accountDao.query(user, owner);
    }

    @Override
    public JSONObject deposit(String user, String owner, int type, int amount, Map<String, String> map) {
        return change(user, owner, type, AccountTypes.DEPOSIT, amount, map);
    }

    @Override
    public JSONObject withdraw(String user, String owner, int type, int amount, Map<String, String> map) {
        return change(user, owner, type, AccountTypes.WITHDRAW, amount, map);
    }

    @Override
    public JSONObject reward(String user, String owner, int type, int amount) {
        return change(user, owner, type, AccountTypes.REWARD, amount, null);
    }

    @Override
    public JSONObject profit(String user, String owner, int type, int amount) {
        return change(user, owner, type, AccountTypes.PROFIT, amount, null);
    }

    @Override
    public JSONObject consume(String user, String owner, int type, int amount) {
        return change(user, owner, type, AccountTypes.CONSUME, amount, null);
    }

    @Override
    public JSONObject remitIn(String user, String owner, int type, int amount) {
        return change(user, owner, type, AccountTypes.REMIT_IN, amount, null);
    }

    @Override
    public JSONObject remitOut(String user, String owner, int type, int amount) {
        return change(user, owner, type, AccountTypes.REMIT_OUT, amount, null);
    }

    private JSONObject change(String user, String owner, int type, String accountType, int amount, Map<String, String> map) {
        AccountModel account = find(user, owner, type);

        return account == null ? null : save(account, accountTypes.get(accountType).change(account, amount, map));
    }

    @Override
    public boolean complete(LogModel log) {
        AccountModel account = accountDao.findById(log.getAccount());
        account = find(account.getUser(), account.getOwner(), account.getType());
        if (account == null || !accountTypes.get(log.getType()).complete(account, log))
            return false;

        save(account);

        return true;
    }

    private AccountModel find(String user, String owner, int type) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        if (validator.isEmpty(owner))
            owner = "";
        String lockId = lockHelper.lock(LOCK_USER + user + "-" + owner + "-" + type, 1000L);
        if (lockId == null)
            return null;

        AccountModel account = accountDao.find(user, owner, type);
        if (account == null) {
            account = new AccountModel();
            account.setUser(user);
            account.setOwner(owner);
            account.setType(type);
            accountDao.save(account);
        }
        account.setLockId(lockId);

        return account;
    }

    private JSONObject save(AccountModel account, String logId) {
        if (logId == null) {
            lockHelper.unlock(account.getLockId());

            return null;
        }

        save(account);
        JSONObject object = modelHelper.toJson(account);
        object.put("logId", logId);

        return object;
    }

    private void save(AccountModel account) {
        account.setChecksum(digest.md5(CHECKSUM + "&" + account.getUser() + "&" + account.getOwner()
                + "&" + account.getType() + "&" + account.getBalance() + "&" + account.getDeposit() + "&" + account.getWithdraw()
                + "&" + account.getReward() + "&" + account.getProfit() + "&" + account.getConsume()
                + "&" + account.getRemitIn() + "&" + account.getRemitOut() + "&" + account.getPending()));
        accountDao.save(account);
        lockHelper.unlock(account.getLockId());
    }
}
