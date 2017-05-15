package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lpw
 */
@Service(AccountTypeSupport.NAME + AccountTypes.PROFIT)
public class ProfitImpl extends AccountTypeSupport implements AccountType {
    @Override
    public String getName() {
        return AccountTypes.PROFIT;
    }

    @Override
    public String change(AccountModel account, int amount, Map<String, String> map) {
        if (amount <= 0)
            return null;

        account.setBalance(account.getBalance() + amount);
        account.setProfit(account.getProfit() + amount);

        return log(account, amount, LogService.State.Complete, map);
    }

    @Override
    public void complte(AccountModel account, LogModel log) {
    }
}
