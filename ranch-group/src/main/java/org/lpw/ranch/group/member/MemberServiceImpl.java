package org.lpw.ranch.group.member;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.group.GroupService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
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
    private Converter converter;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
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
    public JSONArray queryByGroup(String group) {
        String cacheKey = CACHE_GROUP + group;
        JSONArray array = cache.get(cacheKey);
        if (array == null) {
            array = new JSONArray();
            for (MemberModel member : memberDao.queryByGroup(group).getList()) {
                JSONObject object = new JSONObject();
                object.put("id", member.getId());
                object.put("user", userHelper.get(member.getUser()));
                object.put("nick", member.getNick());
                object.put("type", member.getType());
                object.put("join", converter.toString(member.getJoin()));
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
        create(group, owner, null, Type.Owner);
    }

    @Override
    public void join(String group, String reason) {
        String user = userHelper.id();
        JSONObject object = find(group, user);
        if (object.isEmpty()) {
            create(group, user, reason, groupService.get(group).getIntValue("audit") == 0 ? Type.Normal : Type.New);

            return;
        }

        MemberModel member = memberDao.findById(object.getString("id"));
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
        clearCache(member);
    }

    @Override
    public void pass(String id) {
        MemberModel member = findById(id);
        member.setType(Type.Normal.ordinal());
        member.setJoin(dateTime.now());
        memberDao.save(member);
        groupService.member(member.getGroup(), 1);
        clearCache(member);
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
        }
        memberDao.delete(member.getId());
    }

    private void clearCache(MemberModel member) {
        cache.remove(CACHE_GROUP + member.getGroup());
        cache.remove(CACHE_USER + member.getUser());
        cache.remove(CACHE_GROUP_USER + member.getGroup() + member.getUser());
    }
}
