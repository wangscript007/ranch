package org.lpw.ranch.weixin.media;

import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MediaModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Image image;

    @Override
    public String getKey() {
        return MediaModel.NAME;
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return image.is(uploadReader.getContentType(), uploadReader.getFileName());
    }
}
