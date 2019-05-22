package org.lpw.ranch.aliyun;

import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.dao.orm.lite.LiteQuery;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(AliyunModel.NAME + ".dao")
class AliyunDaoImpl implements AliyunDao {
    @Inject
    private LiteOrm liteOrm;

    @Override
    public PageList<AliyunModel> query(int pageSize, int pageNum) {
        return liteOrm.query(new LiteQuery(AliyunModel.class).size(pageSize).page(pageNum), null);
    }

    @Override
    public AliyunModel findById(String id) {
        return liteOrm.findById(AliyunModel.class, id);
    }

    @Override
    public AliyunModel find(String key) {
        return liteOrm.findOne(new LiteQuery(AliyunModel.class).where("c_key=?"), new Object[]{key});
    }

    @Override
    public void save(AliyunModel aliyun) {
        liteOrm.save(aliyun);
    }

    @Override
    public void delete(AliyunModel aliyun) {
        liteOrm.delete(aliyun);
    }
}
