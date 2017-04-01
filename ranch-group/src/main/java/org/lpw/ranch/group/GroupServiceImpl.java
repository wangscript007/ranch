package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.group.member.MemberService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(GroupModel.NAME + ".service")
public class GroupServiceImpl implements GroupService {
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
    @Value("${ranch.message.key:ranch.message}")
    private String messageKey;

    @Override
    public JSONArray queryByUser() {
        JSONArray array = new JSONArray();
        memberService.queryByUser(userHelper.id()).forEach(member -> array.add(getJson(member.getGroup(), null)));

        return array;
    }

    @Override
    public JSONObject get(String[] ids) {
        JSONObject json = new JSONObject();
        for (String id : ids) {
            JSONObject object = getJson(id, null);
            if (!object.isEmpty())
                json.put(id, object);
        }

        return json;
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

        return getJson(group.getId(), null);
    }

    @Override
    public JSONObject name(String id, String name) {
        GroupModel group = groupDao.findById(id);
        group.setName(name);

        return modify(group);
    }

    @Override
    public JSONObject portrait(String id, String portrait) {
        GroupModel group = groupDao.findById(id);
        group.setPortrait(portrait);

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

        return getJson(group.getId(), group);
    }

    @Override
    public JSONObject get(String id) {
        return getJson(id, null);
    }

    private JSONObject getJson(String id, GroupModel group) {
        if (group == null)
            group = groupDao.findById(id);

        return group == null ? new JSONObject() : modelHelper.toJson(group);
    }
}
