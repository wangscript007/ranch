package org.lpw.ranch.editor.screenshot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.element.ElementModel;
import org.lpw.ranch.editor.element.ElementService;
import org.lpw.ranch.temporary.Temporary;
import org.lpw.tephra.chrome.ChromeHelper;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lpw
 */
@Service(ScreenshotModel.NAME + ".service")
public class ScreenshotServiceImpl implements ScreenshotService {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Session session;
    @Inject
    private ChromeHelper chromeHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private Temporary temporary;
    @Inject
    private AsyncService asyncService;
    @Inject
    private EditorService editorService;
    @Inject
    private ElementService elementService;
    @Inject
    private ScreenshotDao screenshotDao;
    @Value("${" + ScreenshotModel.NAME + ".capture.nomark:}")
    private String captureNomark;
    @Value("${" + ScreenshotModel.NAME + ".capture.mark:}")
    private String captureMark;
    @Value("${" + ScreenshotModel.NAME + ".wait:5}")
    private int wait;

    @Override
    public JSONArray query(String editor) {
        return modelHelper.toJson(screenshotDao.query(editor).getList());
    }

    @Override
    public JSONObject find(String editor, String page) {
        return modelHelper.toJson(screenshotDao.find(editor, page));
    }

    @Override
    public String capture(String editor) {
        String sid = session.getId();
        List<ElementModel> elements = elementService.list(editor);

        return asyncService.submit(ScreenshotModel.NAME + ".capture", editor, 2 * elements.size() * wait, () -> {
            Map<String, String> map = capture(sid, editorService.findById(editor), elements, 0, 0);
            JSONArray array = new JSONArray();
            elements.forEach(element -> array.add(map.get(element.getId())));

            return array.toJSONString();
        });
    }

    @Override
    public Map<String, String> capture(String sid, EditorModel editor, List<ElementModel> elements, int width, int height) {
        Map<String, String> map = new HashMap<>();
        if (validator.isEmpty(captureNomark))
            return map;

        Map<String, Integer> index = new HashMap<>();
        if (width > 0 && height > 0)
            capture(captureNomark, sid, editor.getId(), "", width, height, map, index, false);
        elements.forEach(element -> capture(captureNomark, sid, editor.getId(), element.getId(),
                editor.getWidth(), editor.getHeight(), map, index, false));

        screenshotDao.delete(editor.getId());
        map.forEach((page, uri) -> {
            if (page.equals(""))
                editorService.screenshot(editor.getId(), uri);
            ScreenshotModel screenshot = new ScreenshotModel();
            screenshot.setEditor(editor.getId());
            screenshot.setIndex(index.get(page));
            screenshot.setPage(page);
            screenshot.setUri(uri);
            screenshotDao.save(screenshot);
        });

        return map;
    }

    @Override
    public Map<String, String> capture(String sid, EditorModel editor, List<ElementModel> elements, boolean nomark) {
        Map<String, String> map = new HashMap<>();
        String capture = nomark ? captureNomark : captureMark;
        if (!validator.isEmpty(capture))
            elements.forEach(element -> capture(capture, sid, editor.getId(), element.getId(),
                    editor.getWidth(), editor.getHeight(), map, null, true));

        return map;
    }

    private void capture(String capture, String sid, String editor, String page, int width, int height,
                         Map<String, String> map, Map<String, Integer> index, boolean internal) {
        String file = chromeHelper.jpeg(capture + (capture.indexOf('?') == -1 ? "?" : "&") + "sid=" + sid + "&editor=" + editor
                + "&page=" + page, wait, 0, 0, width, height, 100, temporary.root());
        if (validator.isEmpty(file))
            return;

        if (index != null)
            index.put(page, index.size());
        map.put(page, internal ? file : wormholeHelper.image(null, null, null, new File(file)));
    }

    @Override
    public void create(String editor, int index, String page, String uri) {
        ScreenshotModel screenshot = new ScreenshotModel();
        screenshot.setEditor(editor);
        screenshot.setIndex(index);
        screenshot.setPage(page);
        screenshot.setUri(uri);
        screenshotDao.save(screenshot);
    }

    @Override
    public void delete(String editor) {
        screenshotDao.delete(editor);
    }
}
