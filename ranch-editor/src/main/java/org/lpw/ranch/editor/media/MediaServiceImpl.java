package org.lpw.ranch.editor.media;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(MediaModel.NAME + ".service")
public class MediaServiceImpl implements MediaService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MediaDao mediaDao;

    @Override
    public JSONObject query(String editor, int type) {
        return mediaDao.query(validator.isEmpty(editor) ? userHelper.id() : null, editor, type,
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public MediaModel findById(String id) {
        return mediaDao.findById(id);
    }

    @Override
    public JSONObject save(String editor, int type, String url, String name, long size, int width, int height) {
        MediaModel media = new MediaModel();
        media.setUser(userHelper.id());
        media.setEditor(editor);
        media.setType(type);
        media.setUrl(url);
        media.setName(name);
        media.setSize(size);
        media.setWidth(width);
        media.setHeight(height);
        media.setTime(dateTime.now());
        mediaDao.save(media);

        return modelHelper.toJson(media);
    }

    @Override
    public JSONObject name(String id, String name) {
        MediaModel media = findById(id);
        media.setName(name);
        mediaDao.save(media);

        return modelHelper.toJson(media);
    }

    @Override
    public void delete(String id) {
        mediaDao.deleteById(id);
    }

    @Override
    public void deletes(String editor) {
        mediaDao.deleteByEditor(editor);
    }
}
