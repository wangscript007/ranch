package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lpw
 */
@Service(AccountTypeSupport.NAME + AccountTypes.REWARD)
public class RewardImpl extends AccountTypeSupport implements AccountType {
    @Override
    public String getName() {
        return AccountTypes.REWARD;
    }

    @Override
    public String change(AccountModel account, int amount, Map<String, String> map) {
        account.setPending(account.getPending() + amount);

        return log(account, amount, LogService.State.New, map);
    }

    @Override
    public boolean complete(AccountModel account, LogModel log) {
        if (account.getPending() < log.getAmount())
            return false;

        account.setPending(account.getPending() - log.getAmount());
        if (log.getState() != LogService.State.Reject.ordinal()) {
            account.setBalance(account.getBalance() + log.getAmount());
            account.setReward(account.getReward() + log.getAmount());
            log.setBalance(account.getBalance());
        }

        return true;
    }
}
