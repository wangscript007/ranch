package org.lpw.ranch.editor.media;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.role.RoleService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.ctrl.upload.UploadListener;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Numeric;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Controller(MediaModel.NAME + ".upload-listener")
public class UploadListenerImpl implements UploadListener {
    private static final String KEY = "^(" + MediaModel.NAME.replaceAll("\\.", "\\\\.") + "\\.)";

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
        return KEY;
    }

    @Override
    public boolean isUploadEnable(String key, String contentType, String name) {
        String[] array = key.split("\\.");

        return array.length == 5 && userHelper.signIn() && roleService.hasType(null, array[3], RoleService.Type.Editor);
    }

    @Override
    public void complete(JSONObject object) {
        if (!json.hasTrue(object, "success"))
            return;

        String[] array = object.getString("name").split("\\.");
        mediaService.save(array[3], numeric.toInt(array[4]), object.getString("path"), getName(object.getString("fileName")));
    }

    private String getName(String fileName) {
        int indexOf = fileName.lastIndexOf('.');

        return indexOf == -1 ? fileName : fileName.substring(0, indexOf);
    }
}
