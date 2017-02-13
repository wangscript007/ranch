package org.lpw.ranch.gps;

import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.Message;

import javax.inject.Inject;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Inject
    protected Message message;
    @Inject
    protected MockHelper mockHelper;
    @Inject
    protected GpsService gpsService;
}
