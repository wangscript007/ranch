package org.lpw.ranch.account.type;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.log.LogService;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
public abstract class AccountTypeSupport implements AccountType {
    static final String NAME = "ranch.account.type.";

    @Inject
    protected LogService logService;

    protected String log(AccountModel account, int amount, LogService.State state, Map<String, String> map) {
        Map<String, String> parameter = new HashMap<>();
        if (map != null) {
            parameter.putAll(map);
            for (String key : new String[]{"user", "owner", "type", "amount"})
                parameter.remove(key);
        }

        return logService.create(account, getName(), amount, state, parameter);
    }
}
