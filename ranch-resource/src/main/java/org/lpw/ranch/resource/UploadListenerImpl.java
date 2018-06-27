package org.lpw.ranch.resource;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(ResourceModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private UserHelper userHelper;

    @Override
    public String getKey() {
        return ResourceModel.NAME;
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return userHelper.signIn();
    }
}
