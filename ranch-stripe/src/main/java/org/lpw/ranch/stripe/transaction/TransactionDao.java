package org.lpw.ranch.stripe.transaction;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface TransactionDao {
    PageList<TransactionModel> query(String user, Timestamp[] creates, int pageSize, int pageNum);

    int count(String tradeNo);

    void save(TransactionModel transaction);
}
