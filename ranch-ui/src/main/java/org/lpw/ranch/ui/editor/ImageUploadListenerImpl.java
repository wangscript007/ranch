package org.lpw.ranch.ui.editor;

import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.util.Image;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller("ranch.ui.editor.upload-listener.image")
public class ImageUploadListenerImpl implements UploadListener{
    @Inject private Image image;
    @Override
    public String getKey() {
        return "ranch.ui.editor.image";
    }

    @Override
    public boolean isUploadEnable(String key, String contentType, String name) {
        return image.is(contentType, name);
    }
}
