package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author lpw
 */
public abstract class AccountTypeSupport implements AccountType {
    static final String NAME = "ranch.account.type.";

    @Inject
    private LogService logService;
    private Set<String> ignores;

    AccountTypeSupport() {
        ignores = new HashSet<>();
        ignores.add("user");
        ignores.add("owner");
        ignores.add("type");
        ignores.add("channel");
        ignores.add("amount");
    }

    protected String in(AccountModel account, String channel, int amount, Map<String, String> map) {
        account.setPending(account.getPending() + amount);

        return log(account, channel, amount, LogService.State.New, map);
    }

    protected String out(AccountModel account, String channel, int amount, Map<String, String> map) {
        if (account.getBalance() < amount)
            return null;

        account.setBalance(account.getBalance() - amount);
        account.setPending(account.getPending() + amount);

        return log(account, channel, amount, LogService.State.New, map);
    }

    protected String log(AccountModel account, String channel, int amount, LogService.State state, Map<String, String> map) {
        Map<String, String> parameter = new HashMap<>();
        if (map != null) {
            parameter.putAll(map);
            for (String key : ignores)
                parameter.remove(key);
        }

        return logService.create(account, getName(), channel, amount, state, parameter);
    }
}
