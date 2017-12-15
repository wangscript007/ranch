package org.lpw.ranch.form.user;

import java.sql.Timestamp;

/**
 * @author lpw
 */
public interface UserService {
    void create(String form, Timestamp create);

    void delete(String id);
}
