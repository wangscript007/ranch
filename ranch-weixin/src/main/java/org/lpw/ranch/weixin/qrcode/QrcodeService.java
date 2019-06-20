package org.lpw.ranch.weixin.qrcode;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface QrcodeService {
    JSONObject query(String key, String appId, String user, String name, String scene, String[] time);

    JSONObject find(String key, String user, String name);

    QrcodeModel create(String key, String user, String name, String scene);
}
