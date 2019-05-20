package org.lpw.ranch.weixin.template;

import org.lpw.tephra.dao.orm.PageList;

/**
 * @author lpw
 */
interface TemplateDao {
    PageList<TemplateModel> query(String key, String weixinKey, int type, String templateId,int state, int pageSize, int pageNum);

    TemplateModel find(String key);

    void save(TemplateModel template);

    void delete(String id);
}
