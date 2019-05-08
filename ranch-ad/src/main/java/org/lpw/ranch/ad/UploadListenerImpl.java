package org.lpw.ranch.ad;

import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(AdModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Image image;

    @Override
    public String getKey() {
        return AdModel.NAME;
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return image.is(uploadReader.getContentType(), uploadReader.getFileName());
    }
}
