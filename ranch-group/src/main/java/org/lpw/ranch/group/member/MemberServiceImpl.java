package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.group.GroupService;
import org.lpw.ranch.message.helper.MessageHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * @author lpw
 */
@Service(MemberModel.NAME + ".service")
public class MemberServiceImpl implements MemberService {
    @Inject
    private Validator validator;
    @Inject
    private Converter converter;
    @Inject
    private DateTime dateTime;
    @Inject
    private Message message;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MessageHelper messageHelper;
    @Inject
    private GroupService groupService;
    @Inject
    private MemberDao memberDao;

    @Override
    public MemberModel findById(String id) {
        return memberDao.findById(id);
    }

    @Override
    public JSONObject find(String group, String user) {
        MemberModel member = memberDao.find(group, user);

        return member == null ? new JSONObject() : modelHelper.toJson(member);
    }

    @Override
    public JSONArray queryByGroup(String group) {
        JSONObject obj = find(group, userHelper.id());
        int type;
        if (obj.isEmpty() || (type = obj.getIntValue("type")) == 0)
            return new JSONArray();

        if (type >= Type.Manager.ordinal())
            type = 0;
        JSONArray array = new JSONArray();
        for (MemberModel member : memberDao.queryByGroup(group, type).getList()) {
            JSONObject object = modelHelper.toJson(member, converter.toSet(new String[]{"user"}));
            object.put("user", userHelper.get(member.getUser()));
            array.add(object);
        }

        return array;
    }

    @Override
    public List<MemberModel> queryByUser(String user) {
        return memberDao.queryByUser(user).getList();
    }

    @Override
    public void create(String group, String owner) {
        create(group, owner, null, Type.Owner, null);
    }

    @Override
    public void join(String group, String user, String reason, String introducer) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        JSONObject object = find(group, user);
        Type type = groupService.get(group).getIntValue("audit") == 0 ? Type.Normal : Type.New;
        if (type == Type.New && introducer != null) {
            MemberModel member = findById(introducer);
            if (member == null)
                introducer = null;
            else if (member.getType() >= Type.Manager.ordinal())
                type = Type.Normal;
        }
        if (object.isEmpty()) {
            create(group, user, reason, type, introducer);

            return;
        }

        if (object.getIntValue("type") > 0)
            return;

        MemberModel member = memberDao.findById(object.getString("id"));
        member.setType(type.ordinal());
        member.setReason(reason);
        memberDao.save(member);
        if (member.getType() == Type.New.ordinal())
            newcomer(member);
        else
            pass(member);
    }

    private void create(String group, String user, String reason, Type type, String introducer) {
        MemberModel member = new MemberModel();
        member.setGroup(group);
        member.setUser(user);
        member.setReason(reason);
        member.setType(type.ordinal());
        member.setIntroducer(introducer);
        member.setJoin(dateTime.now());
        memberDao.save(member);
        if (member.getType() == Type.New.ordinal())
            newcomer(member);
        else
            pass(member);
    }

    private void newcomer(MemberModel member) {
        memberDao.queryManager(member.getGroup()).getList().forEach(manager -> notice(member, manager.getUser(), "group.member.new", ""));
    }

    @Override
    public void pass(String id) {
        MemberModel member = findById(id);
        if (member.getType() > Type.New.ordinal())
            return;

        member.setType(Type.Normal.ordinal());
        member.setJoin(dateTime.now());
        memberDao.save(member);
        pass(member);
    }

    private void pass(MemberModel member) {
        groupService.member(member.getGroup(), 1);
        if (member.getType() == Type.Owner.ordinal())
            return;

        MemberModel introducer = member.getIntroducer() == null ? null : findById(member.getIntroducer());
        String content = introducer == null ? message.get(MemberModel.NAME + ".pass.join", getNick(member)) :
                message.get(MemberModel.NAME + ".pass", getNick(introducer), getNick(member));
        notice(member, member.getGroup(), "group.member.pass", content);
        notice(member, member.getUser(), "group.member.pass.id", member.getGroup());
    }

    private String getNick(MemberModel member) {
        return validator.isEmpty(member.getNick()) ? userHelper.get(member.getUser()).getString("nick") : member.getNick();
    }

    @Override
    public void refuse(String id) {
        MemberModel member = findById(id);
        if (member == null)
            return;

        memberDao.delete(id);
        notice(member, member.getUser(), "group.member.refuse", message.get(MemberModel.NAME + ".refuse"));
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
        if (member.getType() == Type.Owner.ordinal()) {
            groupService.dismiss(member.getGroup());

            return;
        }

        if (member.getType() >= Type.Normal.ordinal()) {
            groupService.member(member.getGroup(), -1);
            boolean self = member.getUser().equals(userHelper.id());
            notice(member, member.getGroup(), "group.member.leave", message.get(MemberModel.NAME + (self ? ".leave.self" : ".leave"), getNick(member)));
            if (!self)
                notice(member, member.getUser(), "group.member.leave.note", message.get(MemberModel.NAME + ".leave.note"));
        }
        memberDao.delete(member.getId());
    }

    private void notice(MemberModel member, String receiver, String operate, Object message) {
        JSONObject object = new JSONObject();
        object.put("operate", operate);
        object.put("member", getJson(member));
        object.put("message", message);
        messageHelper.notice(MessageHelper.Type.Group, receiver, object.toJSONString(), 0);
    }

    private JSONObject getJson(MemberModel member) {
        JSONObject object = modelHelper.toJson(member);
        object.put("user", userHelper.get(member.getUser()));

        return object;
    }

    @Override
    public void dismiss(String group) {
        memberDao.deleteByGroup(group);
    }
}
