package org.lpw.ranch.recycle;

import net.sf.json.JSONObject;

/**
 * @author lpw
 */
public interface MockRecycleService extends RecycleService {
    String getDeleteId();

    void setRecycle(JSONObject object);

    String getRestoreId();
}
