package org.lpw.ranch.dbtool.table;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.dbtool.schema.SchemaService;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(TableModel.NAME + ".service")
public class TableServiceImpl implements TableService {
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private SchemaService schemaService;
    @Inject
    private TableDao tableDao;

    @Override
    public JSONArray query(String schema, String group, String name) {
        return modelHelper.toJson(tableDao.query(schema, group, name).getList());
    }

    @Override
    public JSONObject save(TableModel table) {
        TableModel model = tableDao.findById(table.getId());
        if (model == null)
            model = new TableModel();
        model.setSchema(table.getSchema());
        model.setSort(table.getSort());
        model.setGroup(table.getGroup());
        model.setName(table.getName());
        model.setMemo(table.getMemo());
        tableDao.save(model);
        schemaService.setTables(model.getSchema(), tableDao.count(model.getSchema()));

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String id) {
        TableModel table = tableDao.findById(id);
        tableDao.delete(table);
        schemaService.setTables(table.getSchema(), tableDao.count(table.getSchema()));
    }
}
