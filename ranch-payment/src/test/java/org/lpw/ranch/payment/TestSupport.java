package org.lpw.ranch.payment;

import org.lpw.tephra.crypto.Sign;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.lpw.tephra.test.DateTimeAspect;
import org.lpw.tephra.test.GeneratorAspect;
import org.lpw.tephra.test.HttpAspect;
import org.lpw.tephra.test.MockHelper;
import org.lpw.tephra.test.PageTester;
import org.lpw.tephra.test.TephraTestSupport;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Generator;
import org.lpw.tephra.util.Json;
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
    Sign sign;
    @Inject
    Message message;
    @Inject
    Generator generator;
    @Inject
    DateTime dateTime;
    @Inject
    Json json;
    @Inject
    Thread thread;
    @Inject
    LiteOrm liteOrm;
    @Inject
    DateTimeAspect dateTimeAspect;
    @Inject
    GeneratorAspect generatorAspect;
    @Inject
    HttpAspect httpAspect;
    @Inject
    PageTester pageTester;
    @Inject
    MockHelper mockHelper;

    PaymentModel create(int i, int state) {
        return create(i, state, new Timestamp(System.currentTimeMillis() - i * TimeUnit.Hour.getTime()));
    }

    PaymentModel create(int i, int state, Timestamp start) {
        PaymentModel payment = new PaymentModel();
        payment.setType("type " + i);
        payment.setUser("user " + i);
        payment.setAmount(100 + i);
        payment.setOrderNo("order no " + i);
        payment.setTradeNo("trade no " + i);
        payment.setState(state);
        payment.setNotify("notify " + i);
        payment.setStart(start);
        payment.setEnd(new Timestamp(System.currentTimeMillis() - i * TimeUnit.Day.getTime()));
        payment.setJson("{\"label\":\"label " + i + "\"}");
        liteOrm.save(payment);

        return payment;

    }

    PaymentModel findByOrderNo(String orderNo) {
        return liteOrm.findOne(new LiteQuery(PaymentModel.class).where("c_order_no=?"), new Object[]{orderNo});
    }
}
