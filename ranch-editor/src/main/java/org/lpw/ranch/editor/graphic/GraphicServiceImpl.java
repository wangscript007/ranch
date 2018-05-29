package org.lpw.ranch.editor.graphic;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(GraphicModel.NAME + ".service")
public class GraphicServiceImpl implements GraphicService {
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private GraphicDao graphicDao;

    @Override
    public JSONObject query(String type, String name) {
        return graphicDao.query(type, name, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public JSONObject save(GraphicModel graphic) {
        if (validator.isEmpty(graphic.getId()) || graphicDao.findById(graphic.getId()) == null)
            graphic.setId(null);
        if (validator.isEmpty(graphic.getType()))
            graphic.setType("");
        graphicDao.save(graphic);

        return modelHelper.toJson(graphic);
    }

    @Override
    public void delete(String id) {
        graphicDao.delete(id);
    }
}
