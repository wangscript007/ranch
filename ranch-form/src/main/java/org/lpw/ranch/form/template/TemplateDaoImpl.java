package org.lpw.ranch.form.template;

import org.lpw.tephra.dao.orm.lite.LiteOrm;
import org.springframework.stereotype.Repository;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Repository(TemplateModel.NAME + ".dao")
class TemplateDaoImpl implements TemplateDao {
    @Inject
    private LiteOrm liteOrm;
}
