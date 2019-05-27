package org.lpw.ranch.user.type;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.facebook.helper.FacebookHelper;
import org.lpw.ranch.user.UserModel;
import org.lpw.tephra.util.Context;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Http;
import org.lpw.tephra.util.Io;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.lpw.tephra.wormhole.WormholeHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author lpw
 */
@Service("ranch.user.type.facebook")
public class FacebookImpl extends TypeSupport {
    @Inject private Validator validator;
    @Inject
    private Context context;
    @Inject
    private Generator generator;
    @Inject
    private Http http;
    @Inject
    private Io io;
    @Inject
    private Logger logger;
    @Inject
    private WormholeHelper wormholeHelper;
    @Inject
    private FacebookHelper facebookHelper;

    @Override
    public int getKey() {
        return Types.FACEBOOK;
    }

    @Override
    public String getUid(String uid, String password) {
        return get(uid, password, "id");
    }

    @Override
    public void signUp(UserModel user, String uid, String password) {
        JSONObject object = getAuth(uid, password);
        if (validator.isEmpty(user.getEmail()) && object.containsKey("email"))
            user.setEmail(object.getString("email"));
        if (validator.isEmpty(user.getNick()) && object.containsKey("name"))
            user.setNick(object.getString("name"));
        if (validator.isEmpty(user.getPortrait()) && object.containsKey("picture")) {
            JSONObject picture = object.getJSONObject("picture");
            if (picture.containsKey("data")) {
                JSONObject data = picture.getJSONObject("data");
                if (data.containsKey("url")) {
                    File file = new File(context.getAbsoluteRoot() + "/" + generator.random(32) + ".jpg");
                    try (OutputStream outputStream = new FileOutputStream(file)) {
                        http.get(data.getString("url"), null, null, null, outputStream);
                    } catch (Throwable e) {
                        logger.warn(e, "下载Facebook头像[{}]时发生异常！", object);
                    }
                    user.setPortrait(wormholeHelper.image(null, null, null, file));
                    io.delete(file);
                }
            }
        }
    }

    @Override
    public String getNick(String uid, String password) {
        return get(uid, password, "name");
    }

    @Override
    public JSONObject getAuth(String uid, String password) {
        String key = "ranch.user.type.facebook:" + uid + "-" + password;
        JSONObject object = context.getThreadLocal(key);
        if (object == null)
            context.putThreadLocal(key, object = facebookHelper.auth(password, uid));

        return object;
    }
}
