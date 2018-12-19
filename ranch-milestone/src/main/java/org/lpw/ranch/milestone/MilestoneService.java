package org.lpw.ranch.milestone;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface MilestoneService {
    JSONObject user();

    JSONObject find(String type);

    JSONObject create(MilestoneModel milestone);
}
