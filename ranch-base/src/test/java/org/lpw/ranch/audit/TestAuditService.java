package org.lpw.ranch.audit;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.recycle.MockRecycleService;

/**
 * @author lpw
 */
public interface TestAuditService extends AuditService, MockRecycleService {
    String[] getPassIds();

    String[] getRefuseIds();

    String getAuditRemark();

    String getDeleteId();

    void setRecycle(JSONObject object);

    String getRestoreId();
}
