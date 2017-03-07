package org.lpw.ranch.friend;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Assert;
import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    Cache cache;
    @Inject
    Converter converter;
    @Inject
    Generator generator;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockHelper mockHelper;

    FriendModel create(String owner, int i) {
        return create(owner, "user " + i, i);
    }

    FriendModel create(String owner, String user, int i) {
        return create(owner, user, "memo " + i, i, i);
    }

    FriendModel create(String owner, int state, int i) {
        return create(owner, "user " + i, "memo " + i, state, i);
    }

    FriendModel create(String owner, String user, String memo, int state, int i) {
        FriendModel model = new FriendModel();
        model.setOwner(owner);
        model.setUser(user);
        model.setMemo(memo);
        model.setState(state);
        model.setCreate(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        liteOrm.save(model);

        return model;
    }

    void equals(List<FriendModel> list, JSONArray array) {
        Assert.assertEquals(list.size(), array.size());
        for (int i = 0; i < list.size(); i++) {
            FriendModel friend = list.get(i);
            JSONObject object = array.getJSONObject(i);
            Assert.assertEquals(friend.getId(), object.getString("id"));
            Assert.assertEquals(friend.getOwner(), object.getString("owner"));
            Assert.assertEquals(friend.getUser(), object.getJSONObject("user").getString("id"));
            if (friend.getMemo() == null)
                Assert.assertFalse(object.containsKey("memo"));
            else
                Assert.assertEquals(friend.getMemo(), object.getString("memo"));
            Assert.assertEquals(friend.getState(), object.getIntValue("state"));
            Assert.assertEquals(converter.toString(friend.getCreate()), object.getString("create"));
        }
    }
}
