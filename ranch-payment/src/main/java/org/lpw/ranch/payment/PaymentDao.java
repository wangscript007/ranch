package org.lpw.ranch.payment;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface PaymentDao {
    PageList<PaymentModel> query(String type, String user, String appId, String orderNo, String tradeNo, int state, Timestamp start, Timestamp end, int pageSize, int pageNum);

    PaymentModel findById(String id);

    PaymentModel findByOrderNo(String orderNo);

    void save(PaymentModel payment);
}
