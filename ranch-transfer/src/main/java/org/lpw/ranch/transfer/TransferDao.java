package org.lpw.ranch.transfer;

import org.lpw.tephra.dao.orm.PageList;

import java.sql.Timestamp;

/**
 * @author lpw
 */
interface TransferDao {
    PageList<TransferModel> query(String type, String appId, String user, String orderNo, String billNo, String tradeNo, int state, Timestamp start, Timestamp end, int pageSize, int pageNum);

    TransferModel findById(String id);

    TransferModel findByOrderNo(String orderNo);

    void save(TransferModel transfer);
}
