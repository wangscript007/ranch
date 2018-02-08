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
    public JSONObject find(String user, String appCode, String type) {
        DeviceModel device = deviceDao.find(user, appCode, type);

        return device == null ? new JSONObject() : modelHelper.toJson(device);
    }

    @Override
    public JSONObject save(String user, String appCode, String type, String macId, String version) {
        if (validator.isEmpty(user))
            user = userHelper.id();
        DeviceModel device = deviceDao.find(user, appCode, type);
        if (device == null) {
            device = new DeviceModel();
            device.setUser(user);
            device.setAppCode(appCode);
            device.setType(type);
        }
        device.setMacId(macId);
        device.setVersion(version);
        device.setTime(dateTime.now());
        deviceDao.save(device);

        return modelHelper.toJson(device);
    }

    @Override
    public void unbind(String user, String appCode, String macId) {
        deviceDao.delete(user, appCode, macId);
    }
}
