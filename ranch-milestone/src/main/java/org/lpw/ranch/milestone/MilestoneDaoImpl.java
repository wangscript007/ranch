package org.lpw.ranch.milestone;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(MilestoneModel.NAME + ".dao")
class MilestoneDaoImpl implements MilestoneDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<MilestoneModel> query(String user, int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(MilestoneModel.class).where("c_user=?")
                .order("c_time desc").size(pageSize).page(pageNum), new Object[]{user});
    }

    @Override
    public MilestoneModel find(String user, String type) {
        return liteOrm.findOne(new LiteQuery(MilestoneModel.class).where("c_user=? and c_type=?"), new Object[]{user, type});
    }

    @Override
    public void save(MilestoneModel milestone) {
        liteOrm.save(milestone);
    }
}
