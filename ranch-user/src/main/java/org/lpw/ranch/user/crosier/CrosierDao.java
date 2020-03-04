package org.lpw.ranch.user.crosier;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface CrosierDao {
    PageList<CrosierModel> query(int grade);

    void save(CrosierModel crosier);

    void delete(int grade);
}
