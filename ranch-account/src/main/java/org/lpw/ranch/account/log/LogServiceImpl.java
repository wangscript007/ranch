package org.lpw.ranch.account.log;

import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.AccountService;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService {
    @Inject
    private DateTime dateTime;
    @Inject
    private AccountService accountService;
    @Inject
    private LogDao logDao;

    @Override
    public String create(AccountModel account, String type, int amount, State state) {
        LogModel log = new LogModel();
        log.setUser(account.getUser());
        log.setAccount(account.getId());
        log.setType(type);
        log.setAmount(amount);
        log.setBalance(account.getBalance());
        log.setState(state.ordinal());
        log.setTime(dateTime.now());
        logDao.save(log);

        return log.getId();
    }

    @Override
    public void complete(String id) {
        LogModel log = logDao.findById(id);
        if (log == null || log.getState() != State.New.ordinal())
            return;

        log.setState(State.Complete.ordinal());
        logDao.save(log);
        accountService.complete(log.getAccount(), log.getAmount());
    }
}
