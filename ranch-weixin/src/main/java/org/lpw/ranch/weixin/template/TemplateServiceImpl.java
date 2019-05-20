package org.lpw.ranch.weixin.template;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.ranch.weixin.WeixinService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(TemplateModel.NAME + ".service")
public class TemplateServiceImpl implements TemplateService {
    @Inject
    private Pagination pagination;
    @Inject
    private WeixinService weixinService;
    @Inject
    private TemplateDao templateDao;

    @Override
    public JSONObject query(String key, String weixinKey, int type, String templateId, int state) {
        return templateDao.query(key, weixinKey, type, templateId, state, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void save(TemplateModel template) {
        TemplateModel model = templateDao.find(template.getKey());
        template.setId(model == null ? null : model.getId());
        templateDao.save(template);
    }

    @Override
    public JSONObject send(String key, String receiver, String formId, JSONObject data) {
        TemplateModel template = templateDao.find(key);
        if (template == null || template.getState() == 0)
            return new JSONObject();

        switch (template.getType()) {
            case 0:
                return weixinService.sendTemplateMessage(template.getWeixinKey(), receiver, template.getTemplateId(), template.getUrl(),
                        template.getMiniAppId(), template.getPage(), data, template.getColor());
            case 1:
                return weixinService.sendMiniTemplateMessage(template.getWeixinKey(), receiver, template.getTemplateId(),
                        template.getPage(), formId, data, template.getKeyword());
            default:
                return new JSONObject();
        }
    }

    @Override
    public void delete(String id) {
        templateDao.delete(id);
    }
}
