package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.util.Validator;

import javax.inject.Inject;

/**
 * @author lpw
 */
abstract class TypeSupport implements Type {
    protected String get(String uid, String password, String name) {
        JSONObject object = getAuth(uid, password);

        return object == null || !object.containsKey(name) ? null : object.getString(name);
    }
}
