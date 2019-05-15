package org.lpw.ranch.google;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;

/**
 * @author lpw
 */
@Service(GoogleModel.NAME + ".service")
public class GoogleServiceImpl implements GoogleService {
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private GoogleDao googleDao;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(googleDao.query().getList());
    }

    @Override
    public GoogleModel findByKey(String key) {
        return googleDao.findByKey(key);
    }

    @Override
    public void save(GoogleModel google) {
        GoogleModel model = googleDao.findByKey(google.getKey());
        google.setId(model == null ? null : model.getId());
        googleDao.save(google);
    }

    @Override
    public void delete(String id) {
        googleDao.delete(id);
    }

    @Override
    public JSONObject auth(String key, String token) {
        GoogleModel google = findByKey(key);
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList(google.getClientId())).build();
        JSONObject object = new JSONObject();
        try {
            GoogleIdToken idToken = verifier.verify(token);
            if (idToken == null) {
                logger.warn(null, "未获得Google登入[{}:{}]认证信息！", key, token);

                return object;
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            object.put("id", payload.getSubject());
            object.put("nick", payload.get("name"));
            object.put("email", payload.getEmail());
            object.put("portrait", payload.get("picture"));
            if (logger.isDebugEnable())
                logger.debug("获得Google登入[{}:{}]认证信息[{}:{}]。", key, token, payload, object);

            return object;
        } catch (Throwable throwable) {
            logger.warn(throwable, "认证Google登入[{}:{}]时发生异常！", key, token);

            return object;
        }
    }
}
