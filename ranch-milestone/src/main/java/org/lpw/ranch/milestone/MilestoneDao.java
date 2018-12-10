package org.lpw.ranch.milestone;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface MilestoneDao {
    PageList<MilestoneModel> query(String user, int pageSize, int pageNum);

    MilestoneModel find(String user, String type);

    void save(MilestoneModel milestone);
}
