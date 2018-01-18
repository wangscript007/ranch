package org.lpw.ranch.device;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.user.helper.UserHelper;
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
    private UserHelper userHelper;
    @Inject
    private DeviceDao deviceDao;

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
