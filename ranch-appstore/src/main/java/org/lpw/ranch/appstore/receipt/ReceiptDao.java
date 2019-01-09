package org.lpw.ranch.appstore.receipt;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface ReceiptDao {
    PageList<ReceiptModel> query(String user, Timestamp[] times, int pageSize, int pageNum);

    void save(ReceiptModel receipt);
}
