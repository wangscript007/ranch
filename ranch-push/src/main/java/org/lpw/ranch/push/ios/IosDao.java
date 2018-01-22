package org.lpw.ranch.push.ios;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface IosDao {
    PageList<IosModel> query(int pageSize, int pageNum);

    IosModel findById(String id);

    IosModel find(String appCode);

    void save(IosModel ios);

    void delete(IosModel ios);
}
