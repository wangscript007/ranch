package org.lpw.ranch.captcha;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface CaptchaDao {
    PageList<CaptchaModel> query();

    CaptchaModel findByKey(String key);

    void save(CaptchaModel captcha);

    void delete(String id);
}
