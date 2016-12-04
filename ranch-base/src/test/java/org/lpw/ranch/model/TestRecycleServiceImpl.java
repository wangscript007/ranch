package org.lpw.ranch.model;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * @author lpw
 */
@Service(TestModel.NAME + ".service")
public class TestRecycleServiceImpl implements TestRecycleService {
    private String deleteId;
    private JSONObject recycle;
    private String restoreId;

    @Override
    public void delete(String id) {
        deleteId = id;
    }

    @Override
    public JSONObject recycle() {
        return recycle;
    }

    @Override
    public void restore(String id) {
        restoreId = id;
    }

    @Override
    public String getDeleteId() {
        return deleteId;
    }

    @Override
    public void setRecycle(JSONObject object) {
        recycle = object;
    }

    @Override
    public String getRestoreId() {
        return restoreId;
    }
}
