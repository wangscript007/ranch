package org.lpw.ranch.appstore.transaction;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface TransactionDao {
    PageList<TransactionModel> query(String user, String transactionId, String productId, Timestamp[] times, int pageSize, int pageNum);

    TransactionModel findByTransactionId(String transactionId);

    void save(TransactionModel transaction);
}
