package org.lpw.ranch.doc.relation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.doc.DocService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(RelationModel.NAME + ".service")
public class RelationServiceImpl implements RelationService {
    @Inject
    private DocService docService;
    @Inject
    private RelationDao relationDao;

    @Override
    public JSONObject find(String doc) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        relationDao.query(doc).getList().forEach(relation -> {
            switch (relation.getType()) {
                case "previous":
                    object.put("previous", docService.find(relation.getRelate(), false));

                    return;
                case "next":
                    object.put("next", docService.find(relation.getRelate(), false));

                    return;
                case "alike":
                    array.add(docService.find(relation.getRelate(), false));
            }
        });
        object.put("relates", array);

        return object;
    }

    @Override
    public void save(String doc, String relate, String type, int sort, boolean close) {
        RelationModel relation = new RelationModel();
        relation.setDoc(doc);
        relation.setRelate(relate);
        relation.setType(type);
        relation.setSort(sort);
        relationDao.save(relation, close);
    }

    @Override
    public void clear() {
        relationDao.clear();
    }
}
