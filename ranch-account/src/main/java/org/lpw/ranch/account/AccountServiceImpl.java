package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.lpw.ranch.account.type.AccountTypes;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.Numeric;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
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
    private Numeric numeric;
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
    private ClassifyHelper classifyHelper;
    @Inject
    private AccountTypes accountTypes;
    @Inject
    private LogService logService;
    @Inject
    private AccountDao accountDao;

    @Override
    public JSONObject query(String uid, String owner, int type, int minBalance, int maxBalance) {
        String user = null;
        if (!validator.isEmpty(uid)) {
            user = userHelper.findIdByUid(uid);
            if (user == null)
                user = uid;
        }
        if ("all".equals(owner))
            owner = null;
        JSONObject object = accountDao.query(user, owner, type, minBalance, maxBalance, pagination.getPageSize(20), pagination.getPageNum()).toJson();
        userHelper.fill(object.getJSONArray("list"), new String[]{"user"});

        return object;
    }

    @Override
    public JSONArray queryUser(String user, String owner, boolean fill) {
        JSONArray array = modelHelper.toJson(queryPageList(user, owner).getList());

        return fill ? userHelper.fill(array, new String[]{"user"}) : array;
    }

    @Override
    public JSONObject merge(String user, String owner, boolean fill) {
        List<AccountModel> list = queryPageList(user, owner).getList();
        AccountModel account = null;
        for (AccountModel model : list) {
            if (model.getOwner().equals("") && model.getType() == 0) {
                account = model;
                break;
            }
        }
        if (account == null)
            return new JSONObject();

        for (AccountModel model : list) {
            if (model.equals(account))
                continue;

            double rate = classifyHelper.valueAsDouble(AccountModel.NAME, "merge.rate." + model.getType(), 1.0D);
            account.setBalance(account.getBalance() + rate(rate, model.getBalance()));
            account.setDeposit(account.getDeposit() + rate(rate, model.getDeposit()));
            account.setWithdraw(account.getWithdraw() + rate(rate, model.getWithdraw()));
            account.setReward(account.getReward() + rate(rate, model.getReward()));
            account.setProfit(account.getProfit() + rate(rate, model.getProfit()));
            account.setConsume(account.getConsume() + rate(rate, model.getConsume()));
            account.setRemitIn(account.getRemitIn() + rate(rate, model.getRemitIn()));
            account.setRemitOut(account.getRemitOut() + rate(rate, model.getRemitOut()));
            account.setPending(account.getPending() + rate(rate, model.getPending()));
        }

        JSONObject object = modelHelper.toJson(account);
        object.put("user", fill ? userHelper.get(user) : user);

        return object;
    }

    private int rate(double rate, int n) {
        return rate == 1.0D ? n : numeric.toInt(rate * n);
    }

    private PageList<AccountModel> queryPageList(String user, String owner) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        if (validator.isEmpty(owner))
            owner = "";
        PageList<AccountModel> pl = queryFromDao(user, owner);
        if (pl.getList().isEmpty()) {
            save(find(user, owner, 0));
            int amount = classifyHelper.valueAsInt(AccountModel.NAME, "reward.sign-up.amount", 0);
            if (amount > 0)
                logService.pass(new String[]{reward(user, owner, classifyHelper.valueAsInt(AccountModel.NAME, "reward.sign-up.type", 0), "sign-up", amount).getString("logId")});
            pl = queryFromDao(user, owner);
        }

        return pl;
    }

    private PageList<AccountModel> queryFromDao(String user, String owner) {
        return accountDao.query(user, owner, -1, -1, -1, 0, 0);
    }

    @Override
    public JSONObject deposit(String user, String owner, int type, String channel, int amount, Map<String, String> map) {
        return change(user, owner, type, AccountTypes.DEPOSIT, channel, amount, map);
    }

    @Override
    public JSONObject withdraw(String user, String owner, int type, String channel, int amount, Map<String, String> map) {
        return change(user, owner, type, AccountTypes.WITHDRAW, channel, amount, map);
    }

    @Override
    public JSONObject reward(String user, String owner, int type, String channel, int amount) {
        return change(user, owner, type, AccountTypes.REWARD, channel, amount, null);
    }

    @Override
    public JSONObject profit(String user, String owner, int type, String channel, int amount) {
        return change(user, owner, type, AccountTypes.PROFIT, channel, amount, null);
    }

    @Override
    public JSONObject consume(String user, String owner, int type, String channel, int amount) {
        return change(user, owner, type, AccountTypes.CONSUME, channel, amount, null);
    }

    @Override
    public JSONObject remitIn(String user, String owner, int type, String channel, int amount) {
        return change(user, owner, type, AccountTypes.REMIT_IN, channel, amount, null);
    }

    @Override
    public JSONObject remitOut(String user, String owner, int type, String channel, int amount) {
        return change(user, owner, type, AccountTypes.REMIT_OUT, channel, amount, null);
    }

    @Override
    public JSONObject refund(String user, String owner, int type, String channel, int amount) {
        return change(user, owner, type, AccountTypes.REFUND, channel, amount, null);
    }

    private JSONObject change(String user, String owner, int type, String accountType, String channel, int amount, Map<String, String> map) {
        AccountModel account = find(user, owner, type);

        return account == null ? null : save(account, accountTypes.get(accountType).change(account, channel, amount, map));
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
                + "&" + account.getRemitIn() + "&" + account.getRemitOut() + "&" + account.getRefund() + "&" + account.getPending()));
        accountDao.save(account);
        lockHelper.unlock(account.getLockId());
    }
}
