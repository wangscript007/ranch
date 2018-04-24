package org.lpw.ranch.user.online;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.UserService;
import org.lpw.ranch.user.auth.AuthModel;
import org.lpw.ranch.user.auth.AuthService;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.ctrl.context.Header;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Service(OnlineModel.NAME + ".service")
public class OnlineServiceImpl implements OnlineService, MinuteJob {
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Header header;
    @Inject
    private Session session;
    @Inject
    private Pagination pagination;
    @Inject
    private UserService userService;
    @Inject
    private AuthService authService;
    @Inject
    private OnlineDao onlineDao;
    @Value("${" + OnlineModel.NAME + ".effective:30}")
    private int effective;

    @Override
    public JSONObject query(String user, String uid, String ip) {
        PageList<OnlineModel> pl = onlineDao.query(getUser(user, uid), ip, pagination.getPageSize(20), pagination.getPageNum());
        JSONObject object = pl.toJson(false);
        JSONArray list = new JSONArray();
        pl.getList().forEach(online -> {
            JSONObject obj = modelHelper.toJson(online);
            obj.put("user", modelHelper.toJson(userService.findById(online.getUser())));
            list.add(obj);
        });
        object.put("list", list);

        return object;
    }

    @Override
    public OnlineModel findBySid(String sid) {
        return onlineDao.findBySid(sid);
    }

    @Override
    public void signIn(String user) {
        String sid = session.getId();
        OnlineModel online = onlineDao.findBySid(sid);
        if (online == null) {
            online = new OnlineModel();
            online.setSid(sid);
        }
        online.setUser(user);
        online.setIp(header.getIp());
        online.setSignIn(dateTime.now());
        online.setLastVisit(dateTime.now());
        onlineDao.save(online);
    }

    @Override
    public boolean isSign() {
        OnlineModel online = onlineDao.findBySid(session.getId());
        if (online == null)
            return false;

        if (System.currentTimeMillis() - online.getLastVisit().getTime() > TimeUnit.Minute.getTime()) {
            online.setLastVisit(dateTime.now());
            onlineDao.save(online);
        }

        return true;
    }

    @Override
    public void signOut() {
        OnlineModel online = onlineDao.findBySid(session.getId());
        if (online != null)
            onlineDao.delete(online);
    }

    @Override
    public void signOut(String user, String uid, String ip) {
        onlineDao.query(getUser(user, uid), ip, 0, 0).getList().forEach(onlineDao::delete);
    }

    private String getUser(String user, String uid) {
        if (!validator.isEmpty(user) || validator.isEmpty(uid))
            return user;

        AuthModel auth = authService.findByUid(uid);

        return auth == null ? uid : auth.getUser();
    }

    @Override
    public void executeMinuteJob() {
        onlineDao.query(new Timestamp(System.currentTimeMillis() - effective * TimeUnit.Minute.getTime())).getList().forEach(online -> {
            userService.signOut(online.getSid());
            onlineDao.delete(online);
        });
    }
}
