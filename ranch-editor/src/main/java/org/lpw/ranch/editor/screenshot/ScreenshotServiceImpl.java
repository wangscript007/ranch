package org.lpw.ranch.editor.screenshot;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorModel;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.editor.element.ElementModel;
import org.lpw.ranch.temporary.Temporary;
import org.lpw.tephra.chrome.ChromeHelper;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Image;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lpw
 */
@Service(ScreenshotModel.NAME + ".service")
public class ScreenshotServiceImpl implements ScreenshotService {
    @Inject
    private Validator validator;
    @Inject
    private Context context;
    @Inject
    private Image image;
    @Inject
    private Logger logger;
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
    private EditorService editorService;
    @Inject
    private ScreenshotDao screenshotDao;
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
    public List<File> capture(String sid, EditorModel editor, List<ElementModel> elements, boolean nomark, boolean save) {
        String capture = nomark ? editorService.getCaptureNomark(sid, editor.getId()) : editorService.getCaptureMark(sid, editor.getId());
        if (capture == null)
            return null;

        int size = elements.size();
        String path = chromeHelper.jpeg(capture, wait, 0, 0, editor.getWidth(), editor.getHeight() * size,
                100, temporary.root());
        if (validator.isEmpty(path))
            return null;

        try {
            List<File> list = new ArrayList<>();
            list.add(new File(path));
            BufferedImage bufferedImage = image.read(new FileInputStream(path));
            if (save)
                screenshotDao.delete(editor.getId());
            for (int i = 0; i < size; i++) {
                File file = new File(context.getAbsolutePath(temporary.newSavePath(".jpeg")));
                image.write(image.subimage(bufferedImage, 0, i * editor.getHeight(), editor.getWidth(), editor.getHeight()),
                        Image.Format.Jpeg, new FileOutputStream(file));
                list.add(file);
                if (i == 0 || !save)
                    continue;

                ScreenshotModel screenshot = new ScreenshotModel();
                screenshot.setEditor(editor.getId());
                screenshot.setIndex(i - 1);
                screenshot.setPage(elements.get(i - 1).getId());
                screenshot.setUri(wormholeHelper.image(null, null, null, file));
                screenshotDao.save(screenshot);
            }

            return list;
        } catch (Throwable throwable) {
            logger.warn(throwable, "截取编辑器图片时发生异常！");

            return null;
        }
    }

    @Override
    public File capture(String sid, String editor, String page, int width, int height) {
        String capture = editorService.getCapture(sid, editor);
        if (capture == null)
            return null;

        String path = chromeHelper.jpeg(capture + "&page=" + page, wait, 0, 0, width, height, 100, temporary.root());

        return validator.isEmpty(path) ? null : new File(path);
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
