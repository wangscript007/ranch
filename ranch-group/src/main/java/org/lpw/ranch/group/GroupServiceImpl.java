package org.lpw.ranch.group;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
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
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private GroupDao groupDao;

    @Override
    public JSONObject create(String name, String note, int audit) {
        GroupModel group = new GroupModel();
        group.setOwner(userHelper.id());
        group.setName(name);
        group.setNote(note);
        group.setMember(1);
        group.setAudit(audit);
        group.setCreate(dateTime.now());
        groupDao.save(group);

        return modelHelper.toJson(group);
    }
}
