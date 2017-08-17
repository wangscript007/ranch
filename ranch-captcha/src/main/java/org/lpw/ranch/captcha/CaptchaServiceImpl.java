package org.lpw.ranch.captcha;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.octo.captcha.Captcha;
import com.octo.captcha.engine.CaptchaEngine;
import org.lpw.tephra.ctrl.context.Session;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.scheduler.MinuteJob;
import org.lpw.tephra.util.Logger;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(CaptchaModel.NAME + ".service")
public class CaptchaServiceImpl implements CaptchaService, MinuteJob {
    private static final String SESSION = CaptchaModel.NAME + ".service.session:";

    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Session session;
    @Inject
    private EngineFactory engineFactory;
    @Inject
    private CaptchaDao captchaDao;
    private Map<String, CaptchaEngine> engines = new ConcurrentHashMap<>();
    private Map<String, String> jsons = new ConcurrentHashMap<>();

    @Override
    public JSONArray query() {
        return modelHelper.toJson(captchaDao.query().getList());
    }

    @Override
    public JSONObject save(CaptchaModel captcha) {
        CaptchaModel model = captchaDao.findByKey(captcha.getKey());
        if (model == null) {
            model = new CaptchaModel();
            model.setKey(captcha.getKey());
        }
        model.setName(captcha.getName());
        model.setWidth(captcha.getWidth());
        model.setHeight(captcha.getHeight());
        model.setFontMin(captcha.getFontMin());
        model.setFontMax(captcha.getFontMax());
        model.setChars(captcha.getChars());
        model.setLength(captcha.getLength());
        model.setBackground(captcha.getBackground());
        captchaDao.save(model);

        return modelHelper.toJson(model);
    }

    @Override
    public void delete(String id) {
        captchaDao.delete(id);
    }

    @Override
    public void image(String key, OutputStream outputStream) {
        CaptchaEngine engine = getEngine(key);
        if (engine == null)
            return;

        try {
            Captcha captcha = engine.getNextCaptcha();
            session.set(SESSION + key, captcha);
            BufferedImage image = (BufferedImage) captcha.getChallenge();
            ImageIO.write(image, "JPEG", outputStream);
        } catch (Throwable e) {
            logger.warn(e, "输出验证码[{}]图片时发生异常！", key);
        }
    }

    @Override
    public boolean validate(String key, String code) {
        if (!jsons.containsKey(key) && captchaDao.findByKey(key) == null)
            return true;

        Captcha captcha = session.get(SESSION + key);

        return captcha != null && captcha.validateResponse(code);
    }

    private CaptchaEngine getEngine(String key) {
        CaptchaEngine engine = engines.get(key);
        if (engine == null) {
            CaptchaModel captcha = captchaDao.findByKey(key);
            if (captcha == null)
                return null;

            engines.put(key, engine = engineFactory.createCaptchaEngine(captcha));
            jsons.put(key, modelHelper.toJson(captcha).toJSONString());
        }

        return engine;
    }

    @Override
    public void executeMinuteJob() {
        Set<String> set = new HashSet<>(jsons.keySet());
        captchaDao.query().getList().forEach(captcha -> {
            set.remove(captcha.getKey());
            if (!jsons.containsKey(captcha.getKey()) || jsons.get(captcha.getKey()).equals(modelHelper.toJson(captcha).toJSONString()))
                return;

            engines.remove(captcha.getKey());
            jsons.remove(captcha.getKey());
        });
        set.forEach(key -> {
            engines.remove(key);
            jsons.remove(key);
        });
    }
}
