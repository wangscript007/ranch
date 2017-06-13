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
    protected LogService logService;
    private Set<String> ignores;

    public AccountTypeSupport() {
        ignores = new HashSet<>();
        ignores.add("user");
        ignores.add("owner");
        ignores.add("type");
        ignores.add("amount");
    }

    protected String log(AccountModel account, int amount, LogService.State state, Map<String, String> map) {
        Map<String, String> parameter = new HashMap<>();
        if (map != null) {
            parameter.putAll(map);
            for (String key : ignores)
                parameter.remove(key);
        }

        return logService.create(account, getName(), amount, state, parameter);
    }
}
