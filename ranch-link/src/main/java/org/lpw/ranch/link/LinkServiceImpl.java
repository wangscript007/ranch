package org.lpw.ranch.link;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.orm.PageList;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Json;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private Pagination pagination;
    @Inject
    private LinkDao linkDao;
    private Set<String> ignores;

    public LinkServiceImpl() {
        ignores = new HashSet<>();
        ignores.add("type");
        ignores.add("id1");
        ignores.add("id2");
    }

    @Override
    public JSONObject query(String type, String id1, String id2) {
        PageList<LinkModel> pl = validator.isEmpty(id1) ? linkDao.query2(type, id2, pagination.getPageSize(20), pagination.getPageNum())
                : linkDao.query1(type, id1, pagination.getPageSize(20), pagination.getPageNum());

        JSONObject object = pl.toJson(false);
        JSONArray array = new JSONArray();
        pl.getList().forEach(link -> array.add(toJson(link)));
        object.put("list", array);

        return object;
    }

    @Override
    public int count(String type, String id1, String id2) {
        return validator.isEmpty(id1) ? linkDao.count2(type, id2) : linkDao.count1(type, id1);
    }

    @Override
    public JSONObject find(String type, String id1, String id2) {
        LinkModel link = linkDao.find(type, id1, id2);

        return link == null ? new JSONObject() : toJson(link);
    }

    @Override
    public JSONObject save(String type, String id1, String id2, Map<String, String> map) {
        LinkModel link = linkDao.find(type, id1, id2);
        if (link == null) {
            link = new LinkModel();
            link.setType(type);
            link.setId1(id1);
            link.setId2(id2);
        }
        JSONObject object = json.toObject(link.getJson());
        if (object == null)
            object = new JSONObject();
        for (String key : map.keySet())
            if (!ignores.contains(key))
                object.put(key, map.get(key));
        if (!object.isEmpty())
            link.setJson(object.toJSONString());
        link.setTime(dateTime.now());
        linkDao.save(link);

        return toJson(link);
    }

    private JSONObject toJson(LinkModel link) {
        JSONObject object = new JSONObject();
        object.put("type", link.getType());
        object.put("id1", link.getId1());
        object.put("id2", link.getId2());
        if (!validator.isEmpty(link.getJson()))
            object.putAll(json.toObject(link.getJson()));
        object.put("time", dateTime.toString(link.getTime()));

        return object;
    }

    @Override
    public void delete(String type, String id1, String id2) {
        linkDao.delete(type, id1, id2);
    }
}
