package org.lpw.ranch.push;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface PushDao {
    PageList<PushModel> query(String key, String subject, int state, int pageSize, int pageNum);

    PushModel findById(String id);

    PushModel find(String key, int state);

    void save(PushModel push);

    void state(String key, int oldState, int newState);

    void delete(String id);
}
