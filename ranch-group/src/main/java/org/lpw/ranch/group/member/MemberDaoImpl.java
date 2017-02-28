package org.lpw.ranch.group.member;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(MemberModel.NAME + ".dao")
class MemberDaoImpl implements MemberDao {
    @Inject
    private LiteOrm liteOrm;
}
