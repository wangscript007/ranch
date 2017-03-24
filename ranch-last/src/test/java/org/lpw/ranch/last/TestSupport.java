package org.lpw.ranch.last;

import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;
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
    Generator generator;
    @Inject
    DateTime dateTime;
    @Inject
    Thread thread;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockHelper mockHelper;
    @Inject
    MockCarousel mockCarousel;
    long time = System.currentTimeMillis();
}
