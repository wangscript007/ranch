package org.lpw.ranch.user.crosier;

import com.alibaba.fastjson.JSONArray;

import java.util.Map;

/**
 * @author lpw
 */
public interface CrosierService {
    JSONArray grades();

    JSONArray pathes(int grade);

    void save(int grade, String pathes);

    boolean permit(String uri, Map<String, String> parameter);
}
