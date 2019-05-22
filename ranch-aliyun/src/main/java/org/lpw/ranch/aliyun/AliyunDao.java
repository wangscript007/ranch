package org.lpw.ranch.aliyun;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface AliyunDao {
    PageList<AliyunModel> query(int pageSize, int pageNum);

    AliyunModel findById(String id);

    AliyunModel find(String key);

    void save(AliyunModel aliyun);

    void delete(AliyunModel aliyun);
}
