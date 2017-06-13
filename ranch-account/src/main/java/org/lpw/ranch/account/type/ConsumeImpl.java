package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogModel;
import org.lpw.ranch.account.log.LogService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lpw
 */
@Service(AccountTypeSupport.NAME + AccountTypes.CONSUME)
public class ConsumeImpl extends AccountTypeSupport implements AccountType {
    @Override
    public String getName() {
        return AccountTypes.CONSUME;
    }

    @Override
    public String change(AccountModel account, int amount, Map<String, String> map) {
        if (account.getBalance() < amount)
            return null;

        account.setBalance(account.getBalance() - amount);
        account.setPending(account.getPending() + amount);

        return log(account, amount, LogService.State.New, map);
    }

    @Override
    public boolean complete(AccountModel account, LogModel log) {
        if (account.getPending() < log.getAmount())
            return false;

        account.setPending(account.getPending() - log.getAmount());
        if (log.getState() == LogService.State.Reject.ordinal()) {
            account.setBalance(account.getBalance() + log.getAmount());
            log.setBalance(log.getBalance() + log.getAmount());
        } else
            account.setConsume(account.getConsume() + log.getAmount());

        return true;
    }
}
