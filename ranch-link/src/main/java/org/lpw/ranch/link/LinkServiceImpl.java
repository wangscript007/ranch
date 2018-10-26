package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(LinkModel.NAME + ".service")
public class LinkServiceImpl implements LinkService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private Json json;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private LinkDao linkDao;

    @Override
    public JSONObject query(String type, String id1, String id2) {
        return (validator.isEmpty(id1) ? linkDao.query2(type, id2, pagination.getPageSize(20), pagination.getPageNum())
                : linkDao.query1(type, id1, pagination.getPageSize(20), pagination.getPageNum())).toJson();
    }

    @Override
    public int count(String type, String id1, String id2) {
        return linkDao.count(type, id1, id2);
    }

    @Override
    public JSONObject find(String type, String id1, String id2) {
        LinkModel link = linkDao.find(type, id1, id2);

        return link == null ? new JSONObject() : modelHelper.toJson(link);
    }

    @Override
    public JSONObject save(LinkModel link) {
        LinkModel model = linkDao.find(link.getType(), link.getId1(), link.getId2());
        if (model == null) {
            model = new LinkModel();
            model.setType(link.getType());
            model.setId1(link.getId1());
            model.setId2(link.getId2());
        }
        model.setJson(link.getJson());
        model.setTime(dateTime.now());
        linkDao.save(model);

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String type, String id1, String id2) {
        linkDao.delete(type, id1, id2);
    }
}
