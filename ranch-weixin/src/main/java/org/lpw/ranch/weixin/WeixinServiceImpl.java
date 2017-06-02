package org.lpw.ranch.weixin;

import com.alibaba.fastjson.JSONArray;
import org.lpw.tephra.dao.model.ModelHelper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(WeixinModel.NAME + ".service")
public class WeixinServiceImpl implements WeixinService {
    @Inject
    private ModelHelper modelHelper;
    @Inject
    private WeixinDao weixinDao;

    @Override
    public JSONArray query() {
        return modelHelper.toJson(weixinDao.query().getList());
    }

    @Override
    public void save(WeixinModel weixin) {
        WeixinModel model = weixinDao.findByKey(weixin.getKey());
        if (model == null) {
            model = new WeixinModel();
            model.setKey(weixin.getKey());
        }
        model.setName(weixin.getName());
        model.setAppId(weixin.getAppId());
        model.setSecret(weixin.getSecret());
        model.setToken(weixin.getToken());
        model.setMchId(weixin.getMchId());
        model.setMchKey(weixin.getMchKey());
        weixinDao.save(model);
    }
}
