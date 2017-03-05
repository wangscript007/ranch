package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.group.GroupService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

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
    public MemberModel findById(String id) {
        return memberDao.findById(id);
    }

    @Override
    public MemberModel find(String group, String user) {
        return memberDao.find(group, user);
    }

    @Override
    public JSONArray queryByGroup(String group) {
        JSONArray array = new JSONArray();
        memberDao.queryByGroup(group).getList().forEach(member -> {
            JSONObject object = new JSONObject();
            object.put("id", member.getId());
            object.put("group", group);
            object.put("user", userHelper.get(member.getUser()));
            object.put("nick", member.getNick());
            array.add(object);
        });

        return array;
    }

    @Override
    public List<MemberModel> queryByUser(String user) {
        return memberDao.queryByUser(user).getList();
    }

    @Override
    public void create(String group, String owner) {
        create(group, owner, null, Type.Owner);
    }

    @Override
    public void join(String group, String reason) {
        String user = userHelper.id();
        MemberModel member = find(group, user);
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
        if (type.ordinal() >= Type.Normal.ordinal())
            groupService.member(group, 1);
    }

    @Override
    public void pass(String id) {
        MemberModel member = findById(id);
        member.setType(Type.Normal.ordinal());
        member.setJoin(dateTime.now());
        memberDao.save(member);
        groupService.member(member.getGroup(), 1);
    }

    @Override
    public void refuse(String id) {
        memberDao.delete(id);
    }

    @Override
    public void manager(String id) {
        MemberModel member = findById(id);
        if (member.getType() >= Type.Manager.ordinal())
            return;

        if (member.getType() == Type.New.ordinal())
            groupService.member(member.getGroup(), 1);
        member.setType(Type.Manager.ordinal());
        memberDao.save(member);
    }

    @Override
    public void nick(String id, String nick) {
        MemberModel member = findById(id);
        member.setNick(nick);
        memberDao.save(member);
    }

    @Override
    public void leave(String id) {
        MemberModel member = findById(id);
        if (member.getType() >= Type.Normal.ordinal())
            groupService.member(member.getGroup(), -1);
        memberDao.delete(member.getId());
    }
}
