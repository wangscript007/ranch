package org.lpw.ranch.device;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(DeviceModel.NAME + ".service")
public class DeviceServiceImpl implements DeviceService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private UserHelper userHelper;
    @Inject
    private DeviceDao deviceDao;

    @Override
    public JSONObject query(String user, String appCode, String type, String macId, String version) {
        JSONObject object = deviceDao.query(userHelper.findIdByUid(user, user), appCode, type, macId, version,
                pagination.getPageSize(20), pagination.getPageNum()).toJson();
        userHelper.fill(object.getJSONArray("list"), new String[]{"user"});

        return object;
    }

    @Override
    public JSONObject find(String appCode, String macId) {
        DeviceModel device = deviceDao.find(appCode, macId);

        return device == null ? new JSONObject() : modelHelper.toJson(device);
    }

    @Override
    public JSONObject save(String user, String appCode, String type, String macId, String version) {
        DeviceModel device = deviceDao.find(appCode, macId);
        if (device == null) {
            device = new DeviceModel();
            device.setAppCode(appCode);
            device.setMacId(macId);
        }
        device.setUser(validator.isEmpty(user) ? userHelper.id() : user);
        device.setType(type);
        device.setVersion(version);
        device.setTime(dateTime.now());
        deviceDao.save(device);

        return modelHelper.toJson(device);
    }
}
