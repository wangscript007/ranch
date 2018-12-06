package org.lpw.ranch.editor.speech;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.wormhole.AuthType;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(SpeechModel.NAME + ".service")
public class SpeechServiceImpl implements SpeechService {
    private static final String CACHE_MODEL = SpeechModel.NAME + ".model:";

    @Inject
    private DateTime dateTime;
    @Inject
    private Generator generator;
    @Inject
    private Json json;
    @Inject
    private Cache cache;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
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
        String cacheKey = CACHE_MODEL + id;
        SpeechModel speech = cache.get(cacheKey);
        if (speech == null)
            cache.put(cacheKey, speech = speechDao.findById(id), false);

        return speech;
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
        speech.setWsUrl(wormholeHelper.getWebSocketUrl());
        speech.setTime(dateTime.now());
        speechDao.save(speech);
        speechDao.setData(speech.getId(), elementService.query(editor, editor, true).toJSONString());

        return modelHelper.toJson(speech);
    }

    @Override
    public void password(String id, String password) {
        SpeechModel speech = findById(id);
        speech.setPassword(password);
    }

    @Override
    public JSONObject produce(String id) {
        SpeechModel speech = findById(id);
        speech.setState(1);
        save(speech);

        return entry(id, AuthType.Producer, speech);
    }

    @Override
    public JSONObject consume(String id) {
        return entry(id, AuthType.Consumer, findById(id));
    }

    private JSONObject entry(String id, AuthType authType, SpeechModel speech) {
        String unique = generator.random(32);
        wormholeHelper.auth(authType, id, unique);
        JSONObject object = new JSONObject();
        object.put("auth", unique);
        object.put("speech", modelHelper.toJson(speech));
        object.put("data", json.toArray(speechDao.getData(id)));

        return object;
    }

    @Override
    public void finish(String id) {
        SpeechModel speech = findById(id);
        speech.setState(2);
        save(speech);
    }

    private void save(SpeechModel speech) {
        speechDao.save(speech);
        cache.remove(CACHE_MODEL + speech.getId());
    }
}
