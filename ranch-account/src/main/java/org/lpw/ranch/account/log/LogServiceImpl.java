package org.lpw.ranch.account.log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.account.AccountModel;
import org.lpw.ranch.account.AccountService;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.scheduler.SecondsJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Date;
import java.util.Map;

/**
 * @author lpw
 */
@Service(LogModel.NAME + ".service")
public class LogServiceImpl implements LogService, SecondsJob {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Json json;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private AccountService accountService;
    @Inject
    private LogDao logDao;

    @Override
    public JSONObject query(String uid, String owner, String type, String channel, int state, Date start, Date end) {
        String userId = null;
        if (!validator.isEmpty(uid)) {
            JSONObject user = userHelper.findByUid(uid);
            userId = user.isEmpty() ? uid : user.getString("id");
        }

        PageList<LogModel> pl = logDao.query(userId, owner, type, channel, state, dateTime.getStart(start), dateTime.getEnd(end), pagination.getPageSize(20), pagination.getPageNum());
        JSONObject object = pl.toJson(false);
        JSONArray list = new JSONArray();
        pl.getList().forEach(log -> list.add(toJson(log)));
        object.put("list", list);

        return object;
    }

    private JSONObject toJson(LogModel log) {
        JSONObject object = modelHelper.toJson(log);
        object.put("user", userHelper.get(log.getUser()));
        if (!validator.isEmpty(log.getJson()))
            object.putAll(json.toObject(log.getJson()));

        return object;
    }

    @Override
    public String create(AccountModel account, String type, String channel, int amount, State state, Map<String, String> map) {
        LogModel log = new LogModel();
        log.setUser(account.getUser());
        log.setAccount(account.getId());
        log.setOwner(account.getOwner());
        log.setType(type);
        log.setChannel(validator.isEmpty(channel) ? "" : channel);
        log.setAmount(amount);
        log.setBalance(account.getBalance());
        log.setState(state.ordinal());
        log.setStart(dateTime.now());
        if (!validator.isEmpty(map)) {
            JSONObject json = new JSONObject();
            map.forEach(json::put);
            log.setJson(json.toJSONString());
        }
        logDao.save(log);

        return log.getId();
    }

    @Override
    public JSONArray pass(String[] ids) {
        JSONArray array = new JSONArray();
        for (String id : ids)
            complete(id, State.Pass, array);

        return array;
    }

    @Override
    public JSONArray reject(String[] ids) {
        JSONArray array = new JSONArray();
        for (String id : ids)
            complete(id, State.Reject, array);

        return array;
    }

    private void complete(String id, State state, JSONArray array) {
        String lockId = lockHelper.lock(LogModel.NAME + ".service.complete:" + id, 1L, 0);
        if (lockId == null)
            return;

        LogModel log = logDao.findById(id);
        if (log == null || log.getState() != State.New.ordinal()) {
            lockHelper.unlock(lockId);

            return;
        }

        log.setState(state.ordinal());
        switch (accountService.complete(log)) {
            case Success:
                if (log.getRestate() > 0)
                    log.setRestate(0);
                log.setEnd(dateTime.now());
                if (array != null)
                    array.add(id);
                break;
            case Locked:
                log.setState(State.New.ordinal());
                log.setRestate(state.ordinal());
                break;
            case Failure:
                lockHelper.unlock(lockId);
                return;
        }
        logDao.save(log);
        lockHelper.unlock(lockId);
    }

    @Override
    public void executeSecondsJob() {
        String lockId = lockHelper.lock(LogModel.NAME + ".service.seconds", 1L, 0);
        if (lockId == null)
            return;

        logDao.query(1).getList().forEach(log -> {
            if (log.getState() == 0)
                complete(log.getId(), State.Pass, null);
        });

        logDao.query(2).getList().forEach(log -> {
            if (log.getState() == 0)
                complete(log.getId(), State.Reject, null);
        });

        lockHelper.unlock(lockId);
    }
}
