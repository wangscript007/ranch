package org.lpw.ranch.editor.screenshot;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ScreenshotModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Image image;
    @Inject
    private UserHelper userHelper;
    @Inject
    private ScreenshotService screenshotService;
    @Inject
    private ScreenshotDao screenshotDao;

    @Override
    public String getKey() {
        return ScreenshotModel.NAME;
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return image.is(uploadReader.getContentType(), uploadReader.getFileName()) && userHelper.grade() >= 50
                && screenshotDao.findById(uploadReader.getParameter("id")) != null;
    }

    @Override
    public void complete(UploadReader uploadReader, JSONObject object) {
        screenshotService.uri(uploadReader.getParameter("id"), object.getString("path"));
    }
}
