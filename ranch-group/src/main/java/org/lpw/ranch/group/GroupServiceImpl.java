package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.group.member.MemberService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(GroupModel.NAME + ".service")
public class GroupServiceImpl implements GroupService {
    private static final String CACHE_ID = GroupModel.NAME + ".service.id:";

    @Inject
    private Cache cache;
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private MemberService memberService;
    @Inject
    private GroupDao groupDao;

    @Override
    public JSONArray queryByUser() {
        JSONArray array = new JSONArray();
        memberService.queryByUser(userHelper.id()).forEach(member -> array.add(getJson(member.getGroup(), null)));

        return array;
    }

    @Override
    public JSONObject create(String name, String note, int audit) {
        GroupModel group = new GroupModel();
        group.setOwner(userHelper.id());
        group.setName(name);
        group.setNote(note);
        group.setAudit(audit);
        group.setCreate(dateTime.now());
        groupDao.save(group);
        memberService.create(group.getId(), group.getOwner());

        return getJson(group.getId(), group);
    }

    @Override
    public JSONObject name(String id, String name) {
        GroupModel group = groupDao.findById(id);
        group.setName(name);

        return modify(group);
    }

    @Override
    public JSONObject note(String id, String note) {
        GroupModel group = groupDao.findById(id);
        group.setNote(note);

        return modify(group);
    }

    @Override
    public JSONObject audit(String id, int audit) {
        GroupModel group = groupDao.findById(id);
        group.setAudit(audit);

        return modify(group);
    }

    @Override
    public void member(String id, int count) {
        GroupModel group = groupDao.findById(id);
        group.setMember(group.getMember() + count);
        modify(group);
    }

    private JSONObject modify(GroupModel group) {
        groupDao.save(group);
        cache.remove(CACHE_ID + group.getId());

        return getJson(group.getId(), group);
    }

    @Override
    public JSONObject get(String id) {
        return getJson(id, null);
    }

    private JSONObject getJson(String id, GroupModel group) {
        String cacheKey = CACHE_ID + id;
        JSONObject object = cache.get(cacheKey);
        if (object == null) {
            if (group == null)
                group = groupDao.findById(id);
            cache.put(cacheKey, object = group == null ? new JSONObject() : modelHelper.toJson(group), false);
        }

        return object;
    }
}
