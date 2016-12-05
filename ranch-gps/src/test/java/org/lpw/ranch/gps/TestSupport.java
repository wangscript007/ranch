package org.lpw.ranch.gps;

import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Autowired
    protected Message message;
    @Autowired
    protected MockHelper mockHelper;
    @Autowired
    protected GpsService gpsService;
}
