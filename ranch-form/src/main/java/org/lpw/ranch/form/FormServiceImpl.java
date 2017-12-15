package org.lpw.ranch.form;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.form.user.UserService;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(FormModel.NAME + ".service")
public class FormServiceImpl implements FormService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private UserService userService;
    @Inject
    private FormDao formDao;

    @Override
    public JSONObject save(String id, String name, String time, int state) {
        FormModel form = validator.isEmpty(id) ? null : formDao.findById(id);
        boolean isNew = form == null;
        if (isNew) {
            form = new FormModel();
            form.setCreate(dateTime.now());
        }
        form.setName(name);
        form.setTime(time);
        form.setState(state);
        form.setModify(dateTime.now());
        formDao.save(form);
        if (isNew)
            userService.create(form.getId(), form.getCreate());

        return modelHelper.toJson(form);
    }
}
