package org.lpw.ranch.model;

import net.sf.json.JSONObject;

/**
 * @author lpw
 */
public interface TestRecycleService extends RecycleService {
    String getDeleteId();

    void setRecycle(JSONObject object);

    String getRestoreId();
}
