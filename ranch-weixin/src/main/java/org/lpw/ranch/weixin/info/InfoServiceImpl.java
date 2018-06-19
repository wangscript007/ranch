package org.lpw.ranch.weixin.info;

import org.lpw.tephra.util.DateTime;
import org.lpw.tephra.util.Validator;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(InfoModel.NAME + ".service")
public class InfoServiceImpl implements InfoService {
    @Inject
    private DateTime dateTime;
    @Inject
    private Validator validator;
    @Inject
    private InfoDao infoDao;

    @Override
    public void save(String key, String appId, String unionId, String openId) {
        if (validator.isEmpty(openId))
            return;

        InfoModel info = infoDao.findByOpenId(openId);
        if (info == null) {
            info = new InfoModel();
            info.setKey(key);
            info.setAppId(appId);
            info.setUnionId(validator.isEmpty(unionId) ? "" : unionId);
            info.setOpenId(openId);
            info.setTime(dateTime.now());
            infoDao.save(info);

            return;
        }

        if (validator.isEmpty(info.getUnionId()) && !validator.isEmpty(unionId)) {
            info.setUnionId(unionId);
            infoDao.save(info);
        }
    }
}
