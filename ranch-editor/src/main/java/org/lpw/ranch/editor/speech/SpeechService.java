package org.lpw.ranch.editor.speech;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface SpeechService {
    String VALIDATOR_CREATE = SpeechModel.NAME + ".validator.create";
    String VALIDATOR_EXISTS = SpeechModel.NAME + ".validator.exists";
    String VALIDATOR_OWNER = SpeechModel.NAME + ".validator.owner";
    String VALIDATOR_PASSWORD = SpeechModel.NAME + ".validator.password";

    JSONObject user(int state, String[] time);

    SpeechModel findById(String id);

    JSONObject info(String id);

    JSONObject create(String editor);

    void password(String id, String password);

    void personal(String id, int personal);

    JSONObject produce(String id);

    JSONObject consume(String id);

    void finish(String id);

    void delete(String id);
}
