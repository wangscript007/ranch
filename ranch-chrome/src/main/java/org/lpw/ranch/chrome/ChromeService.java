package org.lpw.ranch.chrome;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ChromeService {
    /**
     * 内存是否充足验证器Bean名称。
     */
    String VALIDATOR_MEMORY = ChromeModel.NAME + ".validator.memory";
    /**
     * 引用KEY是否存在验证器Bean名称。
     */
    String VALIDATOR_KEY_EXISTS = ChromeModel.NAME + ".validator.key.exists";

    JSONObject query(String key, String name);

    ChromeModel findByKey(String key);

    JSONObject save(ChromeModel chrome);

    void delete(String id);

    byte[] pdf(String key, String url, int width, int height, String pages, int wait);

    byte[] png(String key, String url, int x, int y, int width, int height, int wait);

    byte[] jpg(String key, String url, int x, int y, int width, int height, int wait);
}
