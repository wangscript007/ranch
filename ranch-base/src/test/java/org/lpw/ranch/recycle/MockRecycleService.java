package org.lpw.ranch.recycle;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MockRecycleService extends RecycleService {
    String getDeleteId();

    void setRecycle(JSONObject object);

    String getRestoreId();
}
