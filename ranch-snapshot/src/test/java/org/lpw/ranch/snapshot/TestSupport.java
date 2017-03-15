package org.lpw.ranch.snapshot;

import org.lpw.tephra.ctrl.context.Request;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Message;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    DateTime dateTime;
    @Inject
    LiteOrm liteOrm;
    @Inject
    Request request;
    @Inject
    MockHelper mockHelper;
}
