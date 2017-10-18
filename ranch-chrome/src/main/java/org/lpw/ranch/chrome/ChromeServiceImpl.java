package org.lpw.ranch.chrome;

import com.alibaba.fastjson.JSONObject;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.chrome.Chrome;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ChromeModel.NAME + ".service")
public class ChromeServiceImpl implements ChromeService {

    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private Chrome chrome;
    @Inject
    private ChromeDao chromeDao;

    @Override
    public JSONObject query(String key, String name) {
        return chromeDao.query(key, name, pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public ChromeModel findByKey(String key) {
        return chromeDao.findByKey(key);
    }

    @Override
    public JSONObject save(ChromeModel chrome) {
        ChromeModel model = chromeDao.findByKey(chrome.getKey());
        if (model == null) {
            model = new ChromeModel();
            model.setKey(chrome.getKey());
        }
        model.setName(chrome.getName());
        model.setX(chrome.getX());
        model.setY(chrome.getY());
        model.setWidth(chrome.getWidth());
        model.setHeight(chrome.getHeight());
        model.setPages(chrome.getPages());
        model.setWait(chrome.getWait());
        model.setFilename(chrome.getFilename());
        chromeDao.save(chrome);

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String id) {
        chromeDao.delete(id);
    }

    @Override
    public byte[] pdf(String key, String url, int width, int height, String pages, int wait) {
        ChromeModel chrome = findByKey(key, 0, 0, width, height, pages, wait);

        return this.chrome.pdf(url, chrome.getWait(), chrome.getWidth(), chrome.getHeight(), chrome.getPages());
    }

    @Override
    public byte[] png(String key, String url, int x, int y, int width, int height, int wait) {
        ChromeModel chrome = findByKey(key, x, y, width, height, null, wait);

        return this.chrome.png(url, chrome.getWait(), chrome.getX(), chrome.getY(), chrome.getWidth(), chrome.getHeight());
    }

    @Override
    public byte[] jpg(String key, String url, int x, int y, int width, int height, int wait) {
        ChromeModel chrome = findByKey(key, x, y, width, height, null, wait);

        return this.chrome.png(url, chrome.getWait(), chrome.getX(), chrome.getY(), chrome.getWidth(), chrome.getHeight());
    }

    private ChromeModel findByKey(String key, int x, int y, int width, int height, String pages, int wait) {
        ChromeModel chrome = findByKey(key);
        if (x > 0)
            chrome.setX(x);
        if (y > 0)
            chrome.setY(y);
        if (width > 0)
            chrome.setWidth(width);
        if (height > 0)
            chrome.setHeight(height);
        if (!validator.isEmpty(pages))
            chrome.setPages(pages);
        if (wait > 0)
            chrome.setWait(wait);

        return chrome;
    }
}
