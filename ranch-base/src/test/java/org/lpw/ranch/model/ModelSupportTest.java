package org.lpw.ranch.model;

import org.junit.Assert;
import org.junit.Test;
import org.lpw.tephra.dao.jdbc.Sql;
import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.lpw.tephra.test.TephraTestSupport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author lpw
 */
public class ModelSupportTest extends TephraTestSupport {
    @Autowired
    private Sql sql;
    @Autowired
    private LiteOrm liteOrm;

    @Test
    public void recycle() {
        sql.update("DROP TABLE IF EXISTS t_model;", new Object[0]);
        sql.update("CREATE TABLE t_model\n" +
                "(\n" +
                "  c_id CHAR(36) NOT NULL COMMENT '主键',\n" +
                "  c_recycle INT DEFAULT 0 COMMENT '回收站；0-否，1-是。',\n" +
                "\n" +
                "  PRIMARY KEY pk_model(c_id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;", new Object[0]);

        TestModel model1 = new TestModel();
        model1.setRecycle(Recycle.No.getValue());
        liteOrm.save(model1);
        TestModel model2 = liteOrm.findById(TestModel.class, model1.getId());
        Assert.assertEquals(Recycle.No.getValue(), model2.getRecycle());

        model1.setRecycle(Recycle.Yes.getValue());
        liteOrm.save(model1);
        model2 = liteOrm.findById(TestModel.class, model1.getId());
        Assert.assertEquals(Recycle.Yes.getValue(), model2.getRecycle());
    }
}
