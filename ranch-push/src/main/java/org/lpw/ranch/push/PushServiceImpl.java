package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.device.helper.DeviceHelper;
import org.lpw.ranch.push.log.LogModel;
import org.lpw.ranch.push.log.LogService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.freemarker.Freemarker;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(PushModel.NAME + ".service")
public class PushServiceImpl implements PushService, ContextRefreshedListener {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Json json;
    @Inject
    private Freemarker freemarker;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private DeviceHelper deviceHelper;
    @Inject
    private LogService logService;
    @Inject
    private PushDao pushDao;
    private Map<String, PushSender> senders;

    @Override
    public JSONObject query(String key, String sender, String subject, String template, int state) {
        return pushDao.query(key, sender, subject, template, state, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public PushModel findById(String id) {
        return pushDao.findById(id);
    }

    @Override
    public PushModel findByKey(String key) {
        return pushDao.find(key, 1);
    }

    @Override
    public JSONObject save(String id, String key, String sender, String appCode, String subject, String content,
                           String template, String name, String args, int state) {
        PushModel push = validator.isEmpty(id) ? new PushModel() : pushDao.findById(id);
        if (push == null)
            push = new PushModel();
        push.setKey(key);
        push.setSender(sender);
        push.setAppCode(appCode);
        push.setSubject(subject);
        push.setContent(content);
        push.setTemplate(template);
        push.setName(name);
        push.setArgs(args);
        setState(push, state);

        return modelHelper.toJson(push);
    }

    @Override
    public JSONObject state(String id, int state) {
        PushModel push = pushDao.findById(id);
        setState(push, state);

        return modelHelper.toJson(push);
    }

    private void setState(PushModel push, int state) {
        push.setState(state);
        if (state == 1) {
            pushDao.state(push.getKey(), 1, 0);
            freemarker.putStringTemplate(push.getKey() + ":subject", push.getSubject());
            freemarker.putStringTemplate(push.getKey() + ":content", push.getContent());
        }
        push.setTime(dateTime.now());
        pushDao.save(push);
    }

    @Override
    public void delete(String id) {
        pushDao.delete(id);
    }

    @Override
    public boolean existsSender(String sender) {
        return senders.containsKey(sender);
    }

    @Override
    public boolean send(String key, String user, String receiver, JSONObject args) {
        return send(findByKey(key), user, receiver, args);
    }

    @Override
    public boolean send(String user, String appCode, String subject, String content, JSONObject args) {
        JSONArray devices = deviceHelper.query(user, appCode, null, null, null, 1024, 1)
                .getJSONArray("list");
        if (devices.isEmpty())
            return false;

        for (int i = 0, size = devices.size(); i < size; i++) {
            JSONObject device = devices.getJSONObject(i);
            PushModel push = new PushModel();
            push.setKey("");
            push.setSender(device.getString("type").equals("ios") ? "app.ios" : "app.aliyun");
            push.setAppCode(appCode);
            push.setSubject(subject);
            push.setContent(content);
            push.setTemplate("");
            push.setName("");
            send(push, user, device.getString("macId"), args);
        }

        return true;
    }

    private boolean send(PushModel push, String user, String receiver, JSONObject args) {
        args = getArgs(push, args);
        args.put("badge", logService.unread(receiver, push.getAppCode()));
        LogModel log = logService.create(validator.isEmpty(user) ? userHelper.id() : user, receiver, push, args);
        boolean success = senders.get(push.getSender()).send(push, receiver, args);
        logService.send(log, success);

        return success;
    }

    private JSONObject getArgs(PushModel push, JSONObject args) {
        if (args == null)
            args = new JSONObject();
        if (validator.isEmpty(push.getArgs()))
            return args;

        JSONObject object = json.toObject(push.getArgs());
        if (object == null)
            return args;

        object.putAll(args);

        return object;
    }

    @Override
    public String parse(Type type, String key, String template, JSONObject args) {
        if (validator.isEmpty(args))
            return template;

        String templateKey = key + (type == Type.Subject ? ":subject" : ":content");
        if (!freemarker.containsStringTemplate(templateKey))
            freemarker.putStringTemplate(templateKey, template);

        return freemarker.process(templateKey, args);
    }

    @Override
    public int getContextRefreshedSort() {
        return 31;
    }

    @Override
    public void onContextRefreshed() {
        senders = new HashMap<>();
        BeanFactory.getBeans(PushSender.class).forEach(sender -> senders.put(sender.getName(), sender));
    }
}
