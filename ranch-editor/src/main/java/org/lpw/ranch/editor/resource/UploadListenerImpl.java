package org.lpw.ranch.editor.resource;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ResourceModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Image image;
    @Inject
    private UserHelper userHelper;

    @Override
    public String getKey() {
        return ResourceModel.NAME;
    }

    @Override
    public boolean isUploadEnable(String key, UploadReader uploadReader) {
        String contentType = uploadReader.getContentType();
        String name = uploadReader.getName();

        return (image.is(contentType, name) || (contentType.equals("audio/mpeg") && name.endsWith(".mp3"))
                || (contentType.equals("application/json") && name.endsWith(".json"))) && userHelper.signIn();
    }
}
