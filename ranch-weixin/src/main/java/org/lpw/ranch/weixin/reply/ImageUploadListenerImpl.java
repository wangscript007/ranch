package org.lpw.ranch.weixin.reply;

import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ReplyModel.NAME + ".upload-listener.image")
public class ImageUploadListenerImpl implements UploadListener {
    @Inject
    private Image image;

    @Override
    public String getKey() {
        return ReplyModel.NAME + ".image";
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return image.is(uploadReader.getContentType(), uploadReader.getFileName());
    }
}
