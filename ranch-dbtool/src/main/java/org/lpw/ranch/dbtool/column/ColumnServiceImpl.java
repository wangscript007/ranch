package org.lpw.ranch.dbtool.column;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ColumnModel.NAME + ".service")
public class ColumnServiceImpl implements ColumnService {
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private ColumnDao columnDao;

    @Override
    public JSONArray query(String table, String name) {
        return modelHelper.toJson(columnDao.query(table, name).getList());
    }

    @Override
    public JSONObject save(ColumnModel column) {
        return null;
    }
}
