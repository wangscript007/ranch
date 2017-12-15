package org.lpw.ranch.form.user;

import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
@Service(UserModel.NAME + ".service")
public class UserServiceImpl implements UserService {
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
    @Inject
    private UserDao userDao;

    @Override
    public void create(String form, Timestamp create) {
        UserModel user = new UserModel();
        user.setUser(userHelper.id());
        user.setForm(form);
        user.setCreate(create);
        user.setJoin(dateTime.now());
        userDao.save(user);
    }

    @Override
    public void delete(String id) {
        userDao.delete(id);
    }
}
