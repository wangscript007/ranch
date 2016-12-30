package org.lpw.ranch.user;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.util.Converter;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Component("ranch.user.mock")
public class MockUserImpl implements MockUser {
    @Inject
    private Converter converter;
    @Inject
    private MockCarousel mockCarousel;

    @Override
    public void register() {
        mockCarousel.register("ranch.user.get", (key, header, parameter, cacheable) -> {
            JSONObject object = new JSONObject();
            object.put("code", 0);
            JSONObject data = new JSONObject();
            for (String id : converter.toArray(parameter.get("ids"), ",")) {
                JSONObject user = new JSONObject();
                user.put("id", id);
                user.put("key", key);
                data.put(id, user);
            }
            object.put("data", data);

            return object.toString();
        });
    }

    @Override
    public void verify(JSONObject user, String id) {
        Assert.assertEquals(2, user.size());
        Assert.assertEquals(id, user.getString("id"));
        Assert.assertEquals("ranch.user.get", user.getString("key"));
    }

    @Override
    public void sign(String id) {
        mockCarousel.register("ranch.user.sign", (key, header, parameter, cacheable) -> {
            JSONObject object = new JSONObject();
            object.put("code", 0);
            JSONObject data = new JSONObject();
            data.put("id", id);
            object.put("data", data);

            return object.toString();
        });
    }
}
