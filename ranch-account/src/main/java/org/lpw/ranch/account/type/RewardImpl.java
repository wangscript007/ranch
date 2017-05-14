package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lpw
 */
@Service(AccountTypeSupport.NAME + AccountTypes.REWARD)
public class RewardImpl extends AccountTypeSupport implements AccountType {
    @Value("${ranch.account.reward.audit:false}")
    private boolean audit;

    @Override
    public String getName() {
        return AccountTypes.REWARD;
    }

    @Override
    public String change(AccountModel account, int amount, Map<String, String> map) {
        LogService.State state;
        if (audit) {
            account.setPending(account.getPending() + amount);
            state = LogService.State.New;
        } else {
            account.setBalance(account.getBalance() + amount);
            account.setReward(account.getReward() + amount);
            state = LogService.State.Complete;
        }

        return log(account, amount, state, map);
    }

    @Override
    public void complte(AccountModel account, LogModel log) {
        if (account.getPending() < log.getAmount())
            return;

        account.setPending(account.getPending() - log.getAmount());
        if (log.getState() == LogService.State.Reject.ordinal())
            return;

        account.setBalance(account.getBalance() + log.getAmount());
        account.setReward(account.getReward() + log.getAmount());
        log.setBalance(account.getBalance());
    }
}
