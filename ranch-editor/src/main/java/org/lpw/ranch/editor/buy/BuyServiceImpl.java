package org.lpw.ranch.editor.buy;

import org.lpw.ranch.editor.EditorService;
import org.lpw.tephra.util.DateTime;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(BuyModel.NAME + ".service")
public class BuyServiceImpl implements BuyService {
    @Inject
    private DateTime dateTime;
    @Inject
    private EditorService editorService;
    @Inject
    private BuyDao buyDao;

    @Override
    public BuyModel find(String user, String editor) {
        return buyDao.find(user, editor);
    }

    @Override
    public Map<String, Integer> count() {
        return buyDao.count();
    }

    @Override
    public void create(String user, String editor, int price) {
        if (editorService.findById(editor) == null || buyDao.find(user, editor) != null)
            return;

        BuyModel buy = new BuyModel();
        buy.setUser(user);
        buy.setEditor(editor);
        buy.setPrice(price);
        buy.setTime(dateTime.now());
        buyDao.save(buy);
    }
}
