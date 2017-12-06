package org.lpw.ranch.form.user;

import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(UserModel.NAME + ".service")
public class UserServiceImpl implements UserService {
    @Inject
    private UserDao userDao;
}
