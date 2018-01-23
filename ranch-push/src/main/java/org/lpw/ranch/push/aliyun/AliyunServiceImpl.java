package org.lpw.ranch.push.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import org.lpw.ranch.util.Pagination;
import org.lpw.tephra.dao.model.ModelHelper;
import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Logger;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lpw
 */
@Service(AliyunModel.NAME + ".service")
public class AliyunServiceImpl implements AliyunService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private Logger logger;
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private Pagination pagination;
    @Inject
    private AliyunDao aliyunDao;
    private Map<String, IAcsClient> map = new ConcurrentHashMap<>();

    @Override
    public JSONObject query() {
        return aliyunDao.query(pagination.getPageSize(20), pagination.getPageNum()).toJson();
    }

    @Override
    public AliyunModel find(String appCode) {
        return aliyunDao.find(appCode);
    }

    @Override
    public JSONObject save(String appCode, String keyId, String keySecret, String appKey) {
        AliyunModel aliyun = aliyunDao.find(appCode);
        if (aliyun == null) {
            aliyun = new AliyunModel();
            aliyun.setAppCode(appCode);
        }
        aliyun.setKeyId(keyId);
        aliyun.setKeySecret(keySecret);
        aliyun.setAppKey(appKey);
        aliyun.setTime(dateTime.now());
        aliyunDao.save(aliyun);
        map.remove(appCode);

        return modelHelper.toJson(aliyun);
    }

    @Override
    public void delete(String id) {
        AliyunModel aliyun = aliyunDao.findById(id);
        if (aliyun == null)
            return;

        aliyunDao.delete(aliyun);
        map.remove(aliyun.getAppCode());
    }

    @Override
    public IAcsClient getIAcsClient(String appCode) {
        if (map.containsKey(appCode))
            return map.get(appCode);

        AliyunModel aliyun = aliyunDao.find(appCode);
        if (aliyun == null)
            return null;

        IAcsClient acsClient = new DefaultAcsClient(DefaultProfile.getProfile("cn-hangzhou",
                aliyun.getKeyId(), aliyun.getKeySecret()));
        map.put(appCode, acsClient);

        return acsClient;
    }
}
