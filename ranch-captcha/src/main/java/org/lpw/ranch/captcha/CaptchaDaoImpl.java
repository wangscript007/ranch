package org.lpw.ranch.captcha;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(CaptchaModel.NAME + ".dao")
class CaptchaDaoImpl implements CaptchaDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<CaptchaModel> query() {
        return liteOrm.query(new LiteQuery(CaptchaModel.class), null);
    }

    @Override
    public CaptchaModel findByKey(String key) {
        return liteOrm.findOne(new LiteQuery(CaptchaModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(CaptchaModel captcha) {
        liteOrm.save(captcha);
    }

    @Override
    public void delete(String id) {
        liteOrm.deleteById(CaptchaModel.class, id);
    }
}
