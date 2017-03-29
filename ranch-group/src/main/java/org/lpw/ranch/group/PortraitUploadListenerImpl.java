package org.lpw.ranch.group;

import org.lpw.tephra.ctrl.http.upload.UploadListener;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(GroupModel.NAME + ".upload-listener.portrait")
public class PortraitUploadListenerImpl implements UploadListener {
    @Inject
    private Validator validator;
    private int[] size = new int[]{80, 80};

    @Override
    public String getKey() {
        return GroupModel.NAME + ".portrait";
    }

    @Override
    public boolean isUploadEnable(String key, String contentType, String name) {
        return validator.isImage(contentType, name);
    }

    @Override
    public int[] getImageSize(String key) {
        return size;
    }
}
