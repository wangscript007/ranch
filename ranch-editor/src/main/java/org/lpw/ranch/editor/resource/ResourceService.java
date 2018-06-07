package org.lpw.ranch.editor.resource;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lpw
 */
public interface ResourceService {
    JSONObject query(String type, String name, int state, String user);
}
