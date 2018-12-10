package org.lpw.ranch.milestone;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MilestoneService {
    String VALIDATOR_EXISTS = MilestoneModel.NAME + ".validator.exists";
    String VALIDATOR_NOT_EXISTS = MilestoneModel.NAME + ".validator.not-exists";

    JSONObject user();

    JSONObject find(String user, String type);

    void save(MilestoneModel milestone);

    JSONObject findSave(MilestoneModel milestone);
}
