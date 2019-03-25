package org.lpw.ranch.editor.price;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.util.Pagination;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(PriceModel.NAME + ".service")
public class PriceServiceImpl implements PriceService {
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
        PriceModel model = priceDao.find(price.getType(), price.getGroup());
        price.setId(model == null ? null : model.getId());
        priceDao.save(price);
        editorService.price(null, price.getType(), price.getGroup(), price.getAmount(), price.getVip(), price.getLimited(), price.getTime());
    }

    @Override
    public void delete(String id) {
        priceDao.delete(id);
    }
}
