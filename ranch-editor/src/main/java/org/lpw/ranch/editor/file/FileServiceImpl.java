package org.lpw.ranch.editor.file;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.push.helper.PushHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.scheduler.DateJob;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;

/**
 * @author lpw
 */
@Service(FileModel.NAME + ".service")
public class FileServiceImpl implements FileService, DateJob {
    @Inject
    private Validator validator;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private PushHelper pushHelper;
    @Inject
    private EditorService editorService;
    @Inject
    private FileDao fileDao;

    @Override
    public void save(String editor, String type, File file) {
        save(editor, type, wormholeHelper.file(null, null, null, file), file.length());
    }

    @Override
    public void save(String editor, String type, String uri, long size) {
        FileModel file = fileDao.find(editor, type);
        if (file == null) {
            file = new FileModel();
            file.setEditor(editor);
            file.setType(type);
        }
        file.setUri(uri);
        file.setSize(size);
        fileDao.save(file);
    }

    @Override
    public String download(String editor, String type, String email) {
        FileModel file = fileDao.find(editor, type);
        if (file == null)
            return null;

        String uri = wormholeHelper.temporary(file.getUri());
        if (validator.isEmpty(uri))
            return null;

        file.setDownload(file.getDownload() + 1);
        fileDao.save(file);

        String url = wormholeHelper.getUrl(uri, false);
        if (validator.isEmail(email)) {
            String user = userHelper.id();
            if (validator.isEmpty(user))
                user = UserHelper.SYSTEM_USER_ID;
            JSONObject args = new JSONObject();
            args.put("url", url);
            pushHelper.send(FileModel.NAME + ".download", user, email, args);
        }

        return url;
    }

    @Override
    public void executeDateJob() {
        editorService.download(fileDao.count());
    }
}
