package org.lpw.ranch.push;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.bean.BeanFactory;
import org.lpw.tephra.bean.ContextRefreshedListener;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.freemarker.Freemarker;
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
    private Freemarker freemarker;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private PushDao pushDao;
    private Map<String, PushSender> senders;

    @Override
    public JSONObject query(String key, String subject, int state) {
        return pushDao.query(key, subject, state, pagination.getPageSize(20), pagination.getPageNum()).toJson();
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
    public JSONObject save(String id, String key, String sender, String subject, String content, String template, String name, int state) {
        PushModel push = validator.isEmpty(id) ? new PushModel() : pushDao.findById(id);
        if (push == null)
            push = new PushModel();
        push.setKey(key);
        push.setSender(sender);
        push.setSubject(subject);
        push.setContent(content);
        push.setTemplate(template);
        push.setName(name);
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
    public boolean send(String key, String receiver, JSONObject args) {
        PushModel push = findByKey(key);

        return senders.get(push.getSender()).send(push, receiver, args);
    }

    @Override
    public String parse(Type type, String key, String template, JSONObject args) {
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
