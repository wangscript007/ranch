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
        if (owner != null && owner.trim().length() == 0)
            owner = "";
        PageList<AccountModel> pl = owner == null ? accountDao.query(user) : accountDao.query(user, owner);
        if (pl.getList().isEmpty()) {
            if (balance > 0)
                logService.complete(deposit(user, owner, 0, balance, null).getString("logId"));
            else
                save(find(user, owner, 0));
            pl = owner == null ? accountDao.query(user) : accountDao.query(user, owner);
        }
        JSONArray array = modelHelper.toJson(pl.getList());

        return userHelper.fill(array, new String[]{"user"});
    }

    @Override
    public JSONObject query(String uid) {
        String user = null;
        if (!validator.isEmpty(uid)) {
            user = userHelper.findIdByUid(uid);
            if (user == null)
                user = uid;
        }

        return accountDao.query(user, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject deposit(String user, String owner, int type, int amount, Map<String, String> map) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        AccountModel account = find(user, owner, type);

        return account == null ? null : save(account, accountTypes.get(AccountTypes.DEPOSIT).change(account, amount, map));
    }

    @Override
    public JSONObject withdraw(String owner, int type, int amount, Map<String, String> map) {
        AccountModel account = find(userHelper.id(), owner, type);

        return account == null ? null : save(account, accountTypes.get(AccountTypes.WITHDRAW).change(account, amount, map));
    }

    @Override
    public JSONObject reward(String user, String owner, int type, int amount) {
        AccountModel account = find(user, owner, type);

        return account == null ? null : save(account, accountTypes.get(AccountTypes.REWARD).change(account, amount, null));
    }

    @Override
    public JSONObject profit(String user, String owner, int type, int amount) {
        AccountModel account = find(user, owner, type);

        return account == null ? null : save(account, accountTypes.get(AccountTypes.PROFIT).change(account, amount, null));
    }

    @Override
    public JSONObject consume(String owner, int type, int amount) {
        AccountModel account = find(userHelper.id(), owner, type);

        return account == null ? null : save(account, accountTypes.get(AccountTypes.CONSUME).change(account, amount, null));
    }

    @Override
    public void complete(LogModel log) {
        AccountModel account = accountDao.findById(log.getAccount());
        if (account == null)
            return;

        String lockId = lock(account.getUser());
        if (lockId == null)
            return;

        accountTypes.get(log.getType()).complte(account, log);
        save(account);
    }

    private AccountModel find(String user, String owner, int type) {
        String lockId = lock(user);
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
            accountDao.save(account);
        }
        account.setLockId(lockId);

        return account;
    }

    private String lock(String user) {
        return lockHelper.lock(LOCK_USER + user, 1000L);
    }

    private void save(AccountModel account) {
        StringBuilder sb = new StringBuilder(CHECKSUM);
        for (Object object : new Object[]{account.getUser(), account.getOwner(), account.getType(), account.getBalance(),
                account.getDeposit(), account.getWithdraw(), account.getReward(), account.getProfit(), account.getConsume(), account.getPending()})
            sb.append('&').append(object);
        account.setChecksum(digest.md5(sb.toString()));
        accountDao.save(account);
        lockHelper.unlock(account.getLockId());
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
}
