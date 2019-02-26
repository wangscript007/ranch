package org.lpw.ranch.editor.speech;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.lock.LockHelper;
import org.lpw.ranch.milestone.helper.MilestoneHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.TimeUnit;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.AuthType;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lpw
 */
@Service(SpeechModel.NAME + ".service")
public class SpeechServiceImpl implements SpeechService, MinuteJob {
    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private Json json;
    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private LockHelper lockHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MilestoneHelper milestoneHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private ElementService elementService;
    @Inject
    private SpeechDao speechDao;

    @Override
    public JSONObject user(int state, String[] time) {
        return speechDao.query(userHelper.id(), state, dateTime.toTimeRange(time), pagination.getPageSize(20),
                pagination.getPageNum()).toJson();
    }

    @Override
    public SpeechModel findById(String id) {
        return speechDao.findById(id);
    }

    @Override
    public JSONObject info(String id) {
        SpeechModel speech = findById(id);
        JSONObject object = modelHelper.toJson(speech);
        object.put("password", !validator.isEmpty(speech.getPassword()));
        object.put("owner", speech.getUser().equals(userHelper.id()));

        return object;
    }

    @Override
    public JSONObject create(String editor) {
        EditorModel editorModel = editorService.findById(editor);
        SpeechModel speech = new SpeechModel();
        speech.setUser(userHelper.id());
        speech.setEditor(editor);
        speech.setName(editorModel.getName());
        speech.setWidth(editorModel.getWidth());
        speech.setHeight(editorModel.getHeight());
        speech.setImage(editorModel.getImage());
        speech.setWsUrl(wormholeHelper.getWssUrl());
        speech.setTime(dateTime.now());
        speechDao.save(speech);

        Map<String, String> map = new HashMap<>();
        map.put("id", speech.getId());
        map.put("data", elementService.query(editor, editor, true).toJSONString());
        String string = wormholeHelper.post("/whspeech/save", null, map);
        JSONObject object = json.toObject(string);
        if (!json.has(object, "code", "0"))
            logger.warn(null, "保存演示数据[{}:{}]到Wormhole失败！", map, string);
        milestoneHelper.create(speech.getUser(), SpeechModel.NAME + ".create", null);

        return modelHelper.toJson(speech);
    }

    @Override
    public void password(String id, String password) {
        SpeechModel speech = findById(id);
        speech.setPassword(password);
        speechDao.save(speech);
    }

    @Override
    public void personal(String id, int personal) {
        SpeechModel speech = findById(id);
        speech.setPersonal(personal);
        speechDao.save(speech);
    }

    @Override
    public JSONObject produce(String id) {
        SpeechModel speech = findById(id);
        speech.setState(1);
        speechDao.save(speech);

        return entry(id, AuthType.Producer, speech);
    }

    @Override
    public JSONObject consume(String id) {
        return entry(id, AuthType.Consumer, findById(id));
    }

    private JSONObject entry(String id, AuthType authType, SpeechModel speech) {
        String ticket = generator.random(32);
        if (!wormholeHelper.auth(authType, id, ticket))
            return null;

        JSONObject object = new JSONObject();
        object.put("auth", ticket);
        object.put("speech", modelHelper.toJson(speech));

        return object;
    }

    @Override
    public void finish(String id) {
        SpeechModel speech = findById(id);
        speech.setState(2);
        speechDao.save(speech);
    }

    @Override
    public void delete(String id) {
        speechDao.delete(id);
    }

    @Override
    public void executeMinuteJob() {
        String lockId = lockHelper.lock(SpeechModel.NAME + ".minute", 100, 60);
        if (lockId == null)
            return;

        speechDao.query(2).getList().forEach(speech -> {
            Map<String, String> map = new HashMap<>();
            map.put("auth", speech.getId());
            String string = wormholeHelper.post("/whspeech/outline", null, map);
            JSONObject object = json.toObject(string);
            if (object == null) {
                logger.warn(null, "获取演示[{}]概要[{}]失败！", map, string);
                if (System.currentTimeMillis() - speech.getTime().getTime() < TimeUnit.Day.getTime())
                    return;
            }

            if (speech.getState() == 0)
                speech.setState(1);
            else if (json.hasTrue(object, "finish"))
                speech.setState(2);
            if (object == null)
                speech.setState(2);
            speech.setOutline(string);
            speechDao.save(speech);
        });
        lockHelper.unlock(lockId);
    }
}
