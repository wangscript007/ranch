package org.lpw.ranch.editor.buy;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.editor.EditorService;
import org.lpw.ranch.user.helper.UserHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;

/**
 * @author lpw
 */
@Service(BuyModel.NAME + ".service")
public class BuyServiceImpl implements BuyService {
    @Inject
    private Validator validator;
    @Inject
    private DateTime dateTime;
    @Inject
    private UserHelper userHelper;
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
    public JSONObject purchased(String[] editors) {
        JSONObject object = new JSONObject();
        String user = userHelper.id();
        if (validator.isEmpty(user)) {
            for (String editor : editors)
                object.put(editor, false);

            return object;
        }

        for (String editor : editors)
            object.put(editor, validator.isEmpty(editor) ? "false" : buyDao.find(user, editor) != null);

        return object;
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
