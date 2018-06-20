package org.lpw.ranch.classify;

import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ClassifyModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Image image;

    @Override
    public String getKey() {
        return ClassifyModel.NAME;
    }

    @Override
    public boolean isUploadEnable(String key, UploadReader uploadReader) {
        return image.is(uploadReader.getContentType(), uploadReader.getFileName());
    }
}
