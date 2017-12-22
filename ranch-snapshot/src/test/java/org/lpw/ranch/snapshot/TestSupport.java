package org.lpw.ranch.snapshot;

import org.lpw.tephra.crypto.Digest;
import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Digest digest;
    @Inject
    Message message;
    @Inject
    DateTime dateTime;
    @Inject
    Thread thread;
    @Inject
    Sign sign;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockHelper mockHelper;
}
