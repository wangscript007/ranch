package org.lpw.ranch.address;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.classify.helper.ClassifyHelper;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(AddressModel.NAME + ".service")
public class AddressServiceImpl implements AddressService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserHelper userHelper;
    @Inject
    private ClassifyHelper classifyHelper;
    @Inject
    private AddressDao addressDao;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(addressDao.query(userHelper.id()).getList(), this::getJson);
    }

    @Override
    public JSONObject save(AddressModel address) {
        AddressModel model = validator.isEmpty(address.getId()) ? null : addressDao.findById(address.getId());
        if (model == null)
            address.setId(null);
        address.setUser(userHelper.id());
        if (address.getMajor() == 1)
            addressDao.major(address.getUser(), 0);

        return update(address);
    }

    @Override
    public JSONObject use(String id) {
        return update(addressDao.findById(id));
    }

    @Override
    public JSONObject major(String id) {
        AddressModel address = addressDao.findById(id);
        addressDao.major(address.getUser(), 0);
        address.setMajor(1);

        return update(address);
    }

    private JSONObject update(AddressModel address) {
        address.setTime(dateTime.now());
        addressDao.save(address);

        return getJson(address);
    }

    private JSONObject getJson(AddressModel address) {
        JSONObject object = modelHelper.toJson(address);
        object.put("region", classifyHelper.get(address.getRegion()));

        return object;
    }

    @Override
    public void delete(String id) {
        addressDao.delete(id);
    }

    @Override
    public AddressModel findById(String id) {
        return addressDao.findById(id);
    }
}
