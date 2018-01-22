package org.lpw.ranch.push.ios;

import com.alibaba.fastjson.JSONObject;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.Coder;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(IosModel.NAME + ".service")
public class IosServiceImpl implements IosService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Coder coder;
    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private IosDao iosDao;
    private Map<String, ApnsService> map = new ConcurrentHashMap<>();

    @Override
    public JSONObject query() {
        return iosDao.query(pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public IosModel find(String appCode) {
        return iosDao.find(appCode);
    }

    @Override
    public JSONObject save(String appCode, String p12, String password, String topic, int destination) {
        IosModel ios = iosDao.find(appCode);
        if (ios == null) {
            ios = new IosModel();
            ios.setAppCode(appCode);
        }
        ios.setP12(p12);
        ios.setPassword(password);
        ios.setTopic(topic);
        ios.setDestination(destination);
        ios.setTime(dateTime.now());
        iosDao.save(ios);
        map.remove(appCode);

        return modelHelper.toJson(ios);
    }

    @Override
    public void delete(String id) {
        IosModel ios = iosDao.findById(id);
        if (ios == null)
            return;

        iosDao.delete(ios);
        map.remove(ios.getAppCode());
    }

    @Override
    public ApnsService getApnsService(String appCode) {
        if (validator.isEmpty(appCode))
            return null;

        if (map.containsKey(appCode))
            return map.get(appCode);

        IosModel ios = iosDao.find(appCode);
        if (ios == null)
            return null;

        try {
            InputStream inputStream = new ByteArrayInputStream(coder.decodeBase64(ios.getP12()));
            ApnsServiceBuilder builder = APNS.newService().withCert(inputStream, ios.getPassword());
            if (ios.getDestination() == 0)
                builder.withSandboxDestination();
            else if (ios.getDestination() == 1)
                builder.withProductionDestination();
            ApnsService service = builder.build();
            inputStream.close();
            map.put(appCode, service);

            return service;
        } catch (IOException e) {
            logger.warn(e, "创建APNS服务[{}]时发生异常！", modelHelper.toJson(ios));

            return null;
        }
    }
}
