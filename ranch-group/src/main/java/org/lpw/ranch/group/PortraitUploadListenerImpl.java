package org.lpw.ranch.group;

import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GroupModel.NAME + ".upload-listener.portrait")
public class PortraitUploadListenerImpl implements UploadListener {
    @Inject
    private Image image;
    private int[] size = new int[]{80, 80};

    @Override
    public String getKey() {
        return GroupModel.NAME + ".portrait";
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return image.is(uploadReader.getContentType(), uploadReader.getFileName());
    }

    @Override
    public int[] getImageSize(String key) {
        return size;
    }
}
