package org.lpw.ranch.group.member;

import org.lpw.tephra.cache.Cache;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.MockCarousel;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.lpw.tephra.util.Thread;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    Converter converter;
    @Inject
    Cache cache;
    @Inject
    Thread thread;
    @Inject
    DateTime dateTime;
    @Inject
    LiteOrm liteOrm;
    @Inject
    MockCarousel mockCarousel;
    @Inject
    MockHelper mockHelper;
    long now = System.currentTimeMillis();
}
