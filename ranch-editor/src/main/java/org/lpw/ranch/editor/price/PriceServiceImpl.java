package org.lpw.ranch.editor.price;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(PriceModel.NAME + ".service")
public class PriceServiceImpl implements PriceService {
    @Inject
    private Validator validator;
    @Inject
    private Pagination pagination;
    @Inject
    private EditorService editorService;
    @Inject
    private PriceDao priceDao;

    @Override
    public JSONObject query(String type, String group) {
        return priceDao.query(type, group, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public void save(PriceModel price) {
        PriceModel model = validator.isEmpty(price.getId()) ? null : priceDao.findById(price.getId());
        price.setId(model == null ? null : model.getId());
        priceDao.save(price);
        if (model != null && !model.getGroup().equals(price.getGroup()))
            editorService.group(price.getType(), model.getGroup(), price.getGroup());
        editorService.price(null, price.getType(), price.getGroup(), price.getAmount(), price.getVip(), price.getLimited(), price.getTime());
    }

    @Override
    public void delete(String id) {
        priceDao.delete(id);
    }
}
