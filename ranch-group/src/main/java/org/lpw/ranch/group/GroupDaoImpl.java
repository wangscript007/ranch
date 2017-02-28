package org.lpw.ranch.group;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(GroupModel.NAME + ".dao")
class GroupDaoImpl implements GroupDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public void save(GroupModel group) {
        liteOrm.save(group);
    }
}
