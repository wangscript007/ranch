package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.group.GroupService;
import org.lpw.ranch.message.helper.MessageHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
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
    private static final String CACHE_GROUP = MemberModel.NAME + ".service.group:";
    private static final String CACHE_USER = MemberModel.NAME + ".service.user:";
    private static final String CACHE_GROUP_USER = MemberModel.NAME + ".service.group-user:";

    @Inject
    private Cache cache;
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
        String cacheKey = CACHE_GROUP_USER + group + user;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            MemberModel member = memberDao.find(group, user);
            cache.put(cacheKey, object = member == null ? new JSONObject() : modelHelper.toJson(member), false);
        }

        return object;
    }

    @Override
    public JSONArray queryByGroup(String group, int type) {
        String cacheKey = CACHE_GROUP + group;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = new JSONArray();
            for (MemberModel member : memberDao.queryByGroup(group, type).getList()) {
                JSONObject object = modelHelper.toJson(member, converter.toSet(new String[]{"user"}));
                object.put("user", userHelper.get(member.getUser()));
                array.add(object);
            }
            cache.put(cacheKey, array, false);
        }

        return array;
    }

    @Override
    public List<MemberModel> queryByUser(String user) {
        String cacheKey = CACHE_USER + user;
        List<MemberModel> list = cache.get(cacheKey);
        if (list == null)
            cache.put(cacheKey, list = memberDao.queryByUser(user).getList(), false);

        return list;
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
        clearCache(member);
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
        clearCache(member);
        if (member.getType() == Type.New.ordinal())
            newcomer(member);
        else
            pass(member);
    }

    private void newcomer(MemberModel member) {
        memberDao.queryManager(member.getGroup()).getList().forEach(manager ->
                notify(member, manager.getUser(), "group.new", ""));
    }

    @Override
    public void pass(String id) {
        MemberModel member = findById(id);
        if (member.getType() > Type.New.ordinal())
            return;

        member.setType(Type.Normal.ordinal());
        member.setJoin(dateTime.now());
        memberDao.save(member);
        clearCache(member);
        pass(member);
    }

    private void pass(MemberModel member) {
        groupService.member(member.getGroup(), 1);
        MemberModel introducer = member.getIntroducer() == null ? null : findById(member.getIntroducer());
        String content = introducer == null ? message.get(MemberModel.NAME + ".pass.join", getNick(member)) :
                message.get(MemberModel.NAME + ".pass", getNick(introducer), getNick(member));
        notify(member, member.getGroup(), "group.pass", content);
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
        notify(member, member.getUser(), "group.refuse", message.get(MemberModel.NAME + ".refuse"));
    }

    @Override
    public void manager(String id) {
        MemberModel member = findById(id);
        if (member.getType() >= Type.Manager.ordinal())
            return;

        if (member.getType() == Type.New.ordinal()) {
            groupService.member(member.getGroup(), 1);
            clearCache(member);
        }
        member.setType(Type.Manager.ordinal());
        memberDao.save(member);
    }

    @Override
    public void nick(String id, String nick) {
        MemberModel member = findById(id);
        member.setNick(nick);
        memberDao.save(member);
        clearCache(member);
    }

    @Override
    public void leave(String id) {
        MemberModel member = findById(id);
        if (member.getType() >= Type.Normal.ordinal()) {
            groupService.member(member.getGroup(), -1);
            clearCache(member);
            boolean self = member.getUser().equals(userHelper.id());
            notify(member, member.getGroup(), "group.leave", message.get(MemberModel.NAME + (self ? ".leave.self" : ".leave"), getNick(member)));
            if (!self)
                notify(member, member.getUser(), "group.leave.note", message.get(MemberModel.NAME + ".leave.note"));
        }
        memberDao.delete(member.getId());
    }

    private void clearCache(MemberModel member) {
        cache.remove(CACHE_GROUP + member.getGroup());
        cache.remove(CACHE_USER + member.getUser());
        cache.remove(CACHE_GROUP_USER + member.getGroup() + member.getUser());
    }

    private void notify(MemberModel member, String receiver, String operate, Object message) {
        clearCache(member);
        JSONObject object = new JSONObject();
        object.put("operate", operate);
        object.put("member", getJson(member));
        object.put("message", message);
        messageHelper.notify(MessageHelper.Type.Group, receiver, object.toJSONString());
    }

    private JSONObject getJson(MemberModel member) {
        JSONObject object = modelHelper.toJson(member);
        object.put("user", userHelper.get(member.getUser()));

        return object;
    }
}
