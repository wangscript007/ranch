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
    @Override
    public String getName() {
        return AccountTypes.REWARD;
    }

    @Override
    public String change(AccountModel account, int amount, Map<String, String> map) {
        account.setBalance(account.getBalance() + amount);
        account.setReward(account.getReward() + amount);

        return log(account, amount, LogService.State.Complete, map);
    }

    @Override
    public boolean complete(AccountModel account, LogModel log) {
        return true;
    }
}
