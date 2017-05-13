package org.lpw.ranch.account.type;

import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service("ranch.account.types")
public class AccountTypesImpl implements AccountTypes, ContextRefreshedListener {
    Map<String, AccountType> map;

    @Override
    public AccountType get(String name) {
        return map.get(name);
    }

    @Override
    public int getContextRefreshedSort() {
        return 22;
    }

    @Override
    public void onContextRefreshed() {
        map = new HashMap<>();
        BeanFactory.getBeans(AccountType.class).forEach(type -> map.put(type.getName(), type));
    }
}
