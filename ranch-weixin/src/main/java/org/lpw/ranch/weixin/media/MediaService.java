package org.lpw.ranch.weixin.media;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MediaService {
    String VALIDATOR_EXISTS = MediaModel.NAME + ".validator.exists";

    JSONObject query(String key, String appId, String type, String name, String[] time);

    void create(MediaModel media);

    void delete(String id);
}
