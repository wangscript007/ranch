package org.lpw.ranch.dbtool.schema;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(SchemaModel.NAME + ".service")
public class SchemaServiceImpl implements SchemaService {
    @Inject
    private Validator validator;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private SchemaDao schemaDao;

    @Override
    public JSONArray query(String group, String key, String type, String ip, String name) {
        return modelHelper.toJson(schemaDao.query(group, key, type, ip, name).getList());
    }

    @Override
    public SchemaModel findById(String id) {
        return schemaDao.findById(id);
    }

    @Override
    public SchemaModel findByKey(String key) {
        return schemaDao.findByKey(key);
    }

    @Override
    public JSONObject save(SchemaModel model) {
        SchemaModel schema = validator.isEmpty(model.getId()) ? new SchemaModel() : schemaDao.findById(model.getId());
        schema.setGroup(validator.isEmpty(model.getGroup()) ? null : model.getGroup());
        schema.setKey(model.getKey());
        schema.setType(model.getType());
        schema.setIp(model.getIp());
        schema.setName(model.getName());
        schema.setUsername(model.getUsername());
        schema.setPassword(model.getPassword());
        schema.setMemo(model.getMemo());
        schemaDao.save(schema);

        return modelHelper.toJson(schema);
    }

    @Override
    public void delete(String id) {
        schemaDao.delete(id);
    }
}
