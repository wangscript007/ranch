package org.lpw.ranch.editor.resource;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

/**
 * @author lpw
 */
@Service(ResourceModel.NAME + ".service")
public class ResourceServiceImpl implements ResourceService {
    @Inject
    private ResourceDao resourceDao;

    @Override
    public JSONObject query(String type, String name, int state, String user) {
        return null;
    }
}
