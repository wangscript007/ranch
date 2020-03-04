package org.lpw.ranch.user.crosier;

import com.alibaba.fastjson.JSONArray;

/**
 * @author lpw
 */
public interface CrosierService {
    JSONArray grades();

    JSONArray pathes(int grade);

    void save(int grade, String pathes);
}
