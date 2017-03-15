package org.lpw.ranch.snapshot;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(SnapshotModel.NAME + ".service")
public class SnapshotServiceImpl implements SnapshotService {
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private SnapshotDao snapshotDao;

    @Override
    public JSONObject get(String[] ids) {
        JSONObject object = new JSONObject();
        for (String id : ids) {
            SnapshotModel snapshot = snapshotDao.findById(id);
            if (snapshot != null)
                object.put(id, modelHelper.toJson(snapshot));
        }

        return object;
    }

    @Override
    public String create(String data, String content) {
        SnapshotModel snapshot = new SnapshotModel();
        snapshot.setData(data);
        snapshot.setContent(content);
        snapshot.setTime(dateTime.now());
        snapshotDao.save(snapshot);

        return snapshot.getId();
    }
}
