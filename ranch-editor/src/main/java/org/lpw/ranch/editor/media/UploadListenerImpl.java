package org.lpw.ranch.editor.media;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.ctrl.upload.UploadReader;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MediaModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    @Inject
    private Json json;
    @Inject
    private Numeric numeric;
    @Inject
    private UserHelper userHelper;
    @Inject
    private RoleService roleService;
    @Inject
    private MediaService mediaService;

    @Override
    public String getKey() {
        return MediaModel.NAME;
    }

    @Override
    public boolean isUploadEnable(UploadReader uploadReader) {
        return userHelper.signIn() && roleService.hasType(null, uploadReader.getParameter("editor"), RoleService.Type.Editor);
    }

    @Override
    public void complete(UploadReader uploadReader, JSONObject object) {
        if (!json.hasTrue(object, "success"))
            return;

        object.putAll(mediaService.save(uploadReader.getParameter("editor"), numeric.toInt(uploadReader.getParameter("type")),
                object.getString("path"), getName(object.getString("fileName")), object.getLongValue("fileSize"),
                numeric.toInt(uploadReader.getParameter("width")), numeric.toInt(uploadReader.getParameter("height"))));
    }

    private String getName(String fileName) {
        int indexOf = fileName.lastIndexOf('.');

        return indexOf == -1 ? fileName : fileName.substring(0, indexOf);
    }
}
