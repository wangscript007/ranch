package org.lpw.ranch.push.aliyun;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AliyunDao {
    PageList<AliyunModel> query(int pageSize, int pageNum);

    AliyunModel findById(String id);

    AliyunModel find(String appCode);

    void save(AliyunModel aliyun);

    void delete(AliyunModel aliyun);
}
