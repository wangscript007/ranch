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
        return in(account, amount, map);
    }

    @Override
    public boolean complete(AccountModel account, LogModel log) {
        if (account.getPending() < log.getAmount())
            return false;

        account.setPending(account.getPending() - log.getAmount());
        if (log.getState() == LogService.State.Pass.ordinal()) {
            account.setBalance(account.getBalance() + log.getAmount());
            account.setProfit(account.getProfit() + log.getAmount());
            log.setBalance(account.getBalance());
        }

        return true;
    }
}
