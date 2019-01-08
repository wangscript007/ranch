package org.lpw.ranch.appstore.receipt;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface ReceiptDao {
    PageList<ReceiptModel> query(String productId, Timestamp[] times, int pageSize, int pageNum);

    ReceiptModel find(String md5, int status);

    void save(ReceiptModel receipt);
}
