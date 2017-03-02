package org.lpw.ranch.group.member;

import org.lpw.ranch.group.GroupService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(MemberModel.NAME + ".service")
public class MemberServiceImpl implements MemberService {
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
    @Inject
    private GroupService groupService;
    @Inject
    private MemberDao memberDao;

    @Override
    public void create(String group, String owner) {
        create(group, owner, null, Type.Owner);
    }

    @Override
    public void join(String group, String reason) {
        String user = userHelper.id();
        MemberModel member = memberDao.find(group, user);
        if (member == null) {
            create(group, user, reason, groupService.get(group).getIntValue("audit") == 0 ? Type.Normal : Type.New);

            return;
        }

        member.setReason(reason);
        memberDao.save(member);
    }

    private void create(String group, String user, String reason, Type type) {
        MemberModel member = new MemberModel();
        member.setGroup(group);
        member.setUser(user);
        member.setReason(reason);
        member.setType(type.ordinal());
        member.setJoin(dateTime.now());
        memberDao.save(member);
    }
}
