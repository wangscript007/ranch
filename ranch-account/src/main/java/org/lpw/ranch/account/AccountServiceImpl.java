package org.lpw.ranch.account;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AccountModel.NAME + ".service")
public class AccountServiceImpl implements AccountService {
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccountDao accountDao;

    @Override
    public JSONObject deposit(String owner, int type, int amount) {
        AccountModel account = find(owner, type);

        return modelHelper.toJson(account);
    }

    private AccountModel find(String owner, int type) {
        String user = userHelper.id();
        AccountModel account = accountDao.find(user, owner, type);
        if (account == null) {
            account = new AccountModel();
            account.setUser(user);
            account.setOwner(owner);
            account.setType(type);
        }

        return account;
    }
}
