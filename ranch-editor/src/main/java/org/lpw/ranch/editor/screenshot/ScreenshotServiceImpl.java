package org.lpw.ranch.editor.screenshot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.async.AsyncService;
import org.lpw.tephra.chrome.ChromeHelper;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;

/**
 * @author lpw
 */
@Service(ScreenshotModel.NAME + ".service")
public class ScreenshotServiceImpl implements ScreenshotService {
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Session session;
    @Inject
    private ChromeHelper chromeHelper;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private AsyncService asyncService;
    @Inject
    private ScreenshotDao screenshotDao;
    @Value("${" + ScreenshotModel.NAME + ".capture:}")
    private String capture;

    @Override
    public JSONArray query(String editor) {
        return modelHelper.toJson(screenshotDao.query(editor).getList());
    }

    @Override
    public JSONObject find(String editor, String page) {
        return modelHelper.toJson(screenshotDao.find(editor, page));
    }

    @Override
    public String capture(String editor, String[] pages, int mainWidth, int mainHeight, int pageWidth, int pageHeight) {
        String sid = session.getId();

        return asyncService.submit(ScreenshotModel.NAME + ".capture", "", 2 * pages.length * 10, () -> {
            screenshotDao.delete(editor);
            capture(sid, editor, "", mainWidth, mainHeight);
            for (String page : pages)
                if (!validator.isEmpty(page))
                    capture(sid, editor, page, pageWidth, pageHeight);

            return "";
        });
    }

    private void capture(String sid, String editor, String page, int width, int height) {
        String file = chromeHelper.jpeg(capture + "?sid=" + sid + "&editor=" + editor + "&page=" + page,
                10, 0, 0, width, height, asyncService.root());
        if (validator.isEmpty(file))
            return;

        ScreenshotModel screenshot = new ScreenshotModel();
        screenshot.setEditor(editor);
        screenshot.setPage(page);
        screenshot.setUri(wormholeHelper.image(null, null, null, new File(file)));
        screenshotDao.save(screenshot);
    }
}
