package org.lpw.ranch.chat.friend;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.TimeUnit;

import javax.inject.Inject;
import java.sql.Timestamp;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockHelper mockHelper;

    FriendModel create(String owner, int i) {
        FriendModel friend = new FriendModel();
        friend.setOwner(owner);
        friend.setFriend("friend " + i);
        friend.setMemo("memo " + i);
        friend.setState(i);
        friend.setCreate(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        liteOrm.save(friend);

        return friend;
    }
}
