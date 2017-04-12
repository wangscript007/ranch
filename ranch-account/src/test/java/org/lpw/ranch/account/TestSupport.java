package org.lpw.ranch.account;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    LiteOrm liteOrm;
}
