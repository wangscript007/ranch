package org.lpw.ranch.comment;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.test.mock.MockHelper;
import org.lpw.tephra.util.Converter;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Message;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class TestSupport extends TephraTestSupport {
    @Autowired
    protected Message message;
    @Autowired
    protected Generator generator;
    @Autowired
    protected Converter converter;
    @Autowired
    protected LiteOrm liteOrm;
    @Autowired
    protected MockHelper mockHelper;
}
